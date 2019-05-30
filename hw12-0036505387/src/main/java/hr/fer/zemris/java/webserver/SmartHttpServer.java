package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class SmartHttpServer {

    private String address;
    private String domainName;
    private int port;
    private int workerThreads;
    private int sessionTimeout;
    private Map<String, String> mimeTypes = new HashMap<>();
    private ServerThread serverThread;
    private ExecutorService threadPool;
    private Path documentRoot;

    private Map<String, IWebWorker> workersMap = new HashMap<>();

    private Map<String, SessionMapEntry> sessions = new ConcurrentHashMap<>();
    private Random sessionRandom = new Random();

    public SmartHttpServer(String configFileName) {
        loadServerConfiguration(configFileName);

        serverThread = new ServerThread();
        new CleanerThread().start();
    }

    private void loadServerConfiguration(String configFileName) {
        Properties server = loadProperty(configFileName);

        address = server.getProperty("server.address");
        domainName = server.getProperty("server.domainName");
        port = getIntegerProperty(server, "server.port");
        workerThreads = getIntegerProperty(server, "server.workerThreads");
        documentRoot = getPathProperty(server, "server.documentRoot");
        sessionTimeout = getIntegerProperty(server, "session.timeout");

        loadMimeConfiguration(server.getProperty("server.mimeConfig"));
        loadWorkerConfiguration(server.getProperty("server.workers"));
    }

    private void loadWorkerConfiguration(String filepath)  {
        Properties workers = loadProperty(filepath);

        for (var entry : workers.entrySet()) {
            String path = (String) entry.getKey();
            String fqcn = (String) entry.getValue();

            if (workersMap.containsKey(path)) {
                throw new RuntimeException("Multiple worker definitions for path: " + path);
            }

            try {
                Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
                Object newObject = referenceToClass.newInstance();
                IWebWorker iww = (IWebWorker) newObject;

                workersMap.put(path, iww);
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadMimeConfiguration(String filepath) {
        Properties mimes = loadProperty(filepath);

        mimes.forEach((k, v) -> mimeTypes.put(k.toString(), v.toString()));
    }

    private int getIntegerProperty(Properties properties, String property) {
        return Integer.parseInt(properties.getProperty(property));
    }

    private Path getPathProperty(Properties properties, String property) {
        return Paths.get(properties.getProperty(property));
    }

    private Properties loadProperty(String path) {
        Path filepath = Paths.get(path);
        Properties properties = new Properties();

        try (InputStream is = Files.newInputStream(filepath)) {
            properties.load(is);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

    protected synchronized void start() {
        if (!serverThread.isAlive()) {
            serverThread.start();
        }

        threadPool = Executors.newFixedThreadPool(workerThreads);
    }

    protected synchronized void stop() {
        serverThread.turnOff();

        threadPool.shutdown();
    }

    protected class ServerThread extends Thread {

        private boolean running = false;

        @Override
        public void run() {
            running = true;

            try (ServerSocket serverSocket = new ServerSocket()) {
                serverSocket.bind(new InetSocketAddress((InetAddress) null, port));

                while (true) {
                    if (!running) {
                        break;
                    }

                    Socket client = serverSocket.accept();

                    ClientWorker clientWorker = new ClientWorker(client);

                    threadPool.submit(clientWorker);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void turnOff() {
            running = false;
        }

    }
    private class ClientWorker implements Runnable, IDispatcher {
        private Socket csocket;
        private PushbackInputStream istream;
        private OutputStream ostream;
        private String version;
        private String method;
        private String host;
        private Map<String, String> params = new HashMap<>();
        private Map<String, String> tempParams = new HashMap<>();
        private Map<String, String> permPrams = new HashMap<>();
        private List<RequestContext.RCCookie> outputCookies = new ArrayList<>();
        private String SID;

        private RequestContext context;

        public ClientWorker(Socket csocket) {
            super();
            this.csocket = csocket;
        }

        @Override
        public void run() {
            try {
                unsafelyRun();
            } catch (Exception e) {
                try {
                    returnError(500, "Internal server error.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void unsafelyRun() throws Exception {
            istream = new PushbackInputStream(csocket.getInputStream());
            ostream = csocket.getOutputStream();

            List<String> request = readRequest();

            if (request.size() < 1) {
                returnError(400, "Invalid header");
                return;
            }

            String firstLine = request.get(0);

            String[] parts = firstLine.split("\\s+");

            method = parts[0];
            String requestedPath = parts[1];
            version = parts[2];

            if (!method.equalsIgnoreCase("GET") ||
                !(version.equalsIgnoreCase("HTTP/1.0") || version.equalsIgnoreCase("HTTP/1.1"))) {
                returnError(400, "Invalid header");
                return;
            }

            Optional<String> hostLine = request.stream().filter(s -> s.startsWith("Host:")).findFirst();

            if (hostLine.isPresent()) {
                host = hostLine.get()
                        .substring(5)                                 // Remove "Host:"
                        //         01234
                        .strip()                                      // Remove leading and trailing spaces
                        .replaceFirst(":\\d+", ""); // Remove port

            }

            String path;
            String paramString = "";

            String[] partsOfPath = requestedPath.split("\\?");

            path = partsOfPath[0];

            if (partsOfPath.length > 1) {
                paramString = partsOfPath[1];
            }

            checkSession(request);

            parseParameters(paramString);

            internalDispatchRequest(path, true);
        }

        private void checkSession(List<String> request) {
            List<String> cookies = request.stream()
                                          .filter(l -> l.startsWith("Cookie: "))
                                          .collect(Collectors.toList());

            String sidCandidate = null;

            for (String cookieLine : cookies) {
                String values = cookieLine.replaceAll("Cookie:\\s?", "");
                String[] nameAndValues = values.split(";");

                for (String nameAndValue : nameAndValues) {
                    String[] parts = nameAndValue.split("=");

                    String name = parts[0];
                    String value = parts[1].substring(1, parts[1].length() - 1);

                    if (name.equalsIgnoreCase("sid")) {
                        sidCandidate = value;
                    }
                    else {
                        outputCookies.add(new RequestContext.RCCookie(name, value, null,
                                host == null ? domainName : host, null));
                    }
                }
            }

            synchronized (SmartHttpServer.this) {
                if (sidCandidate != null) {
                    SessionMapEntry entry = sessions.get(sidCandidate);

                    if (entry == null || !entry.host.equals(host)) {
                        sidCandidate = null;
                    }
                    else if (entry.validUntil < System.currentTimeMillis()) {
                        sessions.remove(entry);
                    }
                    else {
                        entry.validUntil = System.currentTimeMillis() + sessionTimeout * 1000; // Seconds -> milliseconds
                    }
                }

                if (sidCandidate == null) {
                    sidCandidate = generateSessionID();

                    SessionMapEntry entry = new SessionMapEntry(sidCandidate,
                            host == null ? domainName : host, System.currentTimeMillis() + sessionTimeout * 1000);
                                                                   // Seconds -> milliseconds

                    sessions.put(sidCandidate, entry);
                }

                outputCookies.add(new RequestContext.RCCookie("sid", sidCandidate, null,
                        host == null ? domainName : host, "/", true));

                SessionMapEntry entry = sessions.get(sidCandidate);

                SID = sidCandidate;
                permPrams = entry.map;
            }
        }

        private String generateSessionID() {
            char[] sid = new char[20];

            synchronized (SmartHttpServer.this) {
                for (int index = 0; index < sid.length; index++) {
                    sid[index] = (char) ('A' + (abs(sessionRandom.nextInt()) % 26));
                }
            }

            return new String(sid);
        }

        private void returnError(int statusCode, String statusText) throws IOException {
            if (context == null) {
                context = new RequestContext(ostream, null, null, null,
                                              null, this,  SID);
            }

            context.setStatusCode(statusCode);
            context.setStatusText(statusText);

            context.write("");
            ostream.flush();
            csocket.close();
        }

        private void parseParameters(String paramString) {
            if (paramString.isBlank()) {
                return;
            }

            String[] splitParams = paramString.split("&");

            for (String param : splitParams) {
                String[] splitParam = param.split("=");

                params.put(splitParam[0], splitParam[1]);
            }
        }

        private List<String> readRequest() throws IOException {
            String request = getRequestHeader();
                                   // Handle multiline headers by removing the new line character
            String[] lines = request.replaceAll("\n(\t| )", "$1")
                                    .split("\n");

            return List.of(lines);
        }

        private String getRequestHeader() throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // End of request is \r\n\r\n
            int endOfRequest = ('\r' << 24) | ('\n' << 16) | ('\r' << 8) | ('\n' << 0);
            int lastFourBytes = 0;

            while (true) {
                int b = istream.read();

                // If the stream ends before the requests are ended, return null
                if (b == -1) {
                    return null;
                }

                // Remove \r characters from the output, so only \n is left
                if (b != '\r') {
                    bos.write(b);
                }

                // Remove least recent byte by shifting it out and add the most recent byte
                lastFourBytes = (lastFourBytes << 8) | b;

                if (lastFourBytes == endOfRequest) {
                    break;
                }
            }

            return new String(bos.toByteArray(), StandardCharsets.US_ASCII);
        }

        public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
            if (workersMap.containsKey(urlPath)) {
                if (context == null) {
                    context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
                }

                workersMap.get(urlPath).processRequest(context);

                ostream.flush();
                csocket.close();
                return;
            }
            else if (urlPath.startsWith("/ext/")) {
                // /ext/
                // 01234
                executeWorker(urlPath.substring(5));
                return;
            }
            else if (urlPath.startsWith("/private") && directCall) {
                returnError(404, "File not found.");
                return;
            }

            Path requestedFilePath = documentRoot.resolve(Paths.get(urlPath.substring(1)));

            if (!requestedFilePath.startsWith(documentRoot)) {
                returnError(403, "Forbidden");
                return;
            }

            String mime = null;

            if (!Files.isReadable(requestedFilePath)) {
                returnError(404, "File not found");
                return;
            }
            else {
                int index = urlPath.lastIndexOf('.');

                if (index != -1) {
                    String extension = urlPath.substring(index + 1);

                    if ("smscr".equalsIgnoreCase(extension)) {
                        handleSmartScript(requestedFilePath);
                        return;
                    }

                    mime = mimeTypes.get(extension);
                }
            }

            if (mime == null) {
                mime = "application/octet-stream";
            }

            if (context == null) {
                context = new RequestContext(ostream, params, permPrams, outputCookies, null, this, SID);
            }

            context.setMimeType(mime);
            context.setContentLength(Files.size(requestedFilePath));
            context.write(Files.readAllBytes(requestedFilePath));
            ostream.flush();
            csocket.close();
        }

        private void executeWorker(String className) throws IOException {
            try {
                String fqcn = "hr.fer.zemris.java.webserver.workers." + className;
                Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
                Object newObject = referenceToClass.newInstance();
                IWebWorker iww = (IWebWorker) newObject;

                if (context == null) {
                    context = new RequestContext(ostream, params, permPrams, outputCookies, null, this, SID);
                }

                iww.processRequest(context);

                ostream.flush();
                csocket.close();
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                returnError(404, "Worker not found");
            } catch (Exception e) {
                returnError(404, "Worker not found");
                e.printStackTrace();
            }
        }

        private void handleSmartScript(Path file) throws IOException {
            String contents = Files.readString(file);
            SmartScriptParser parser = new SmartScriptParser(contents);

            if (context == null) {
                context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
            }

            SmartScriptEngine engine = new SmartScriptEngine(parser.getDocumentNode(), context);

            engine.execute();

            ostream.flush();
            csocket.close();
        }

        @Override
        public void dispatchRequest(String urlPath) throws Exception {
            internalDispatchRequest(urlPath, false);
        }
    }

    private static class SessionMapEntry {
        private String sid;
        private String host;
        private long validUntil;
        private Map<String, String> map;

        public SessionMapEntry(String sid, String host, long validUntil) {
            this.sid = sid;
            this.host = host;
            this.validUntil = validUntil;
            this.map = new ConcurrentHashMap<>();
        }
    }

    private class CleanerThread extends Thread {

        public CleanerThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (SmartHttpServer.this) {
                        // Remove expired sessions
                        sessions.entrySet()
                                .stream()
                                .filter(e -> e.getValue().validUntil < System.currentTimeMillis())
                                .map(Map.Entry<String, SessionMapEntry>::getKey)
                                .forEach(sessions::remove);
                    }

                    Thread.sleep(5 * 60 * 1000); // Sleep 5 minutes
                } catch (InterruptedException ignored) {}
            }
        }

    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Program expects one argument: path to main configuration file.");
            return;
        }

        SmartHttpServer server = new SmartHttpServer(args[0]);

        server.start();
    }
}