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

/**
 * Models a simple HTTP server capable of running web workers, executing Smart Scripts and fetching resources.
 *
 * @author Marko Lazarić
 */
public class SmartHttpServer {

    /**
     * The address on which the server listens.
     */
    private String address;

    /**
     * The domain name of the server.
     */
    private String domainName;

    /**
     * The port of the server.
     */
    private int port;

    /**
     * The number of workers in the thread pool.
     */
    private int workerThreads;

    /**
     * The session timeout.
     */
    private int sessionTimeout;

    /**
     * The mapping of file extensions to mime types.
     */
    private Map<String, String> mimeTypes = new HashMap<>();

    /**
     * The server thread currently running.
     */
    private ServerThread serverThread;

    /**
     * The thread pool for answering requests to the server.
     */
    private ExecutorService threadPool;

    /**
     * The document root used for resolving relative paths.
     */
    private Path documentRoot;

    /**
     * The mapping of paths to workers.
     */
    private Map<String, IWebWorker> workersMap = new HashMap<>();

    /**
     * The mapping of session IDs to sessions.
     */
    private Map<String, SessionMapEntry> sessions = new ConcurrentHashMap<>();

    /**
     * The random generator for generating session IDs.
     */
    private Random sessionRandom = new Random();

    /**
     * Creates a new {@link SmartHttpServer} with the given argument.
     *
     * @param configFileName the path to the server configuration
     */
    public SmartHttpServer(String configFileName) {
        loadServerConfiguration(configFileName);

        serverThread = new ServerThread();
        new CleanerThread().start();
    }

    /**
     * Loads server configuration from the config file.
     *
     * @param configFileName the path to the server configuration
     */
    private void loadServerConfiguration(String configFileName) {
        Properties server = loadProperty(configFileName);

        address = server.getProperty("server.address");
        domainName = server.getProperty("server.domainName");
        port = Integer.parseInt(server.getProperty("server.port"));
        workerThreads = Integer.parseInt(server.getProperty("server.workerThreads"));
        documentRoot = Paths.get(server.getProperty("server.documentRoot"));
        sessionTimeout = Integer.parseInt(server.getProperty("session.timeout"));

        loadMimeConfiguration(server.getProperty("server.mimeConfig"));
        loadWorkerConfiguration(server.getProperty("server.workers"));
    }

    /**
     * Loads worker configuration from the config file.
     *
     * @param filepath the path to the worker configuration
     *
     * @throws RuntimeException if it encounters an error while creating instances of the workers
     */
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

    /**
     * Loads mime type configuration from the config file.
     *
     * @param filepath the path to the mime type configuration
     */
    private void loadMimeConfiguration(String filepath) {
        Properties mimes = loadProperty(filepath);

        mimes.forEach((k, v) -> mimeTypes.put(k.toString(), v.toString()));
    }

    /**
     * Loads a configuration file.
     *
     * @param path the path to the configuration file
     * @return the loaded configuration file as a {@link Properties} object
     *
     * @throws RuntimeException if the file is not parsable or is not readable
     */
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

    /**
     * Starts the server.
     */
    protected synchronized void start() {
        if (!serverThread.isAlive()) {
            serverThread.start();
        }

        threadPool = Executors.newFixedThreadPool(workerThreads);
    }

    /**
     * Stops the server.
     */
    protected synchronized void stop() {
        serverThread.turnOff();

        threadPool.shutdown();
    }

    /**
     * Models a server thread which responds to requests.
     *
     * @author Marko Lazarić
     */
    protected class ServerThread extends Thread {

        /**
         * Whether the thread is currently running.
         */
        private volatile boolean running = false;

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

        /**
         * Turns the thread off.
         */
        public void turnOff() {
            running = false;
        }

    }

    /**
     * Models a single client worker which responds to a single request.
     *
     * @author Marko Lazarić
     */
    private class ClientWorker implements Runnable, IDispatcher {

        /**
         * The socket of the request.
         */
        private Socket csocket;

        /**
         * The input stream of the request.
         */
        private PushbackInputStream istream;

        /**
         * The output stream for the response.
         */
        private OutputStream ostream;

        /**
         * The version of HTTP.
         */
        private String version;

        /**
         * The method of the request.
         */
        private String method;

        /**
         * The host the request was made to.
         */
        private String host;

        /**
         * The GET parameters.
         */
        private Map<String, String> params = new HashMap<>();

        /**
         * The temporary parameters.
         */
        private Map<String, String> tempParams = new HashMap<>();

        /**
         * The persistent parameters.
         */
        private Map<String, String> permPrams = new HashMap<>();

        /**
         * The output cookies.
         */
        private List<RequestContext.RCCookie> outputCookies = new ArrayList<>();

        /**
         * The session ID.
         */
        private String SID;

        /**
         * The context of the request.
         */
        private RequestContext context;

        /**
         * Creates a new {@link ClientWorker} with the given argument.
         *
         * @param csocket the socket of the request
         *
         * @throws NullPointerException if {@code csocket} is {@code null}
         */
        public ClientWorker(Socket csocket) {
            super();
            this.csocket = Objects.requireNonNull(csocket, "Socket cannot be null.");
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

        /**
         * Respond to a request made to the server.
         *
         * @throws Exception if an error occurs while responding to the request
         */
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

        /**
         * Checks the session of the request and either extends it or creates a new one.
         *
         * @param request the header lines of the request
         */
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

        /**
         * Generates a session ID made of 20 upper case letters.
         *
         * @return the generated session ID
         */
        private String generateSessionID() {
            char[] sid = new char[20];

            synchronized (SmartHttpServer.this) {
                for (int index = 0; index < sid.length; index++) {
                    sid[index] = (char) ('A' + (abs(sessionRandom.nextInt()) % 26));
                }
            }

            return new String(sid);
        }

        /**
         * Returns an error response with the specified parameters.
         *
         * @param statusCode the status code of the error
         * @param statusText the status text of the error
         *
         * @throws IOException if an error occurs while respond to the request
         */
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

        /**
         * Parses parameters from the parameter string.
         *
         * @param paramString the string to parse parameters from
         */
        private void parseParameters(String paramString) {
            if (paramString.isBlank()) {
                return;
            }

            String[] splitParams = paramString.split("&");

            for (String param : splitParams) {
                String[] splitParam = param.split("=");

                if (splitParam.length == 2) {
                    params.put(splitParam[0], splitParam[1]);
                }
                else if (splitParam.length == 1) { // If the name was given without the value
                    params.put(splitParam[0], "");
                }
            }
        }

        /**
         * Reads the header lines from the request into a list.
         *
         * @return the list of the header lines
         *
         * @throws IOException if an error occurs while reading the header lines
         */
        private List<String> readRequest() throws IOException {
            String request = getRequestHeader();
                                   // Handle multiline headers by removing the new line character
            String[] lines = request.replaceAll("\n(\t| )", "$1")
                                    .split("\n");

            return List.of(lines);
        }

        /**
         * Extracts the header from the request.
         *
         * @return the header extracted from the request
         * @throws IOException if an error occurs while extracting the header
         */
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

        /**
         * Dispatch the request internally.
         *
         * @param urlPath the path which was requested
         * @param directCall whether it is a direct call or not
         *
         * @throws Exception if an error occurs while dispatching the request
         */
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

        /**
         * Executes the specified web worker.
         *
         * @param className the fully qualified class name of the web worker to execute
         *
         * @throws IOException if an error occurs while executing the web worker
         */
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

        /**
         * Executes a smart script file.
         *
         * @param file the path to the smart script
         *
         * @throws IOException if an error occurs while executing the smart script file
         */
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

    /**
     * Models a single session map entry.
     *
     * @author Marko Lazarić
     */
    private static class SessionMapEntry {

        /**
         * The session ID.
         */
        private String sid;

        /**
         * The session host.
         */
        private String host;

        /**
         * The expiration time of the session.
         */
        private long validUntil;

        /**
         * The map of persistent parameters.
         */
        private Map<String, String> map;

        /**
         * Creates a new {@link SessionMapEntry} with the given arguments.
         *
         * @param sid the session ID
         * @param host the session host
         * @param validUntil the expiration time of the session
         */
        public SessionMapEntry(String sid, String host, long validUntil) {
            this.sid = sid;
            this.host = host;
            this.validUntil = validUntil;
            this.map = new ConcurrentHashMap<>();
        }
    }

    /**
     * Cleaner thread which runs every 5 minutes and deletes expired sessions.
     *
     * @author Marko Lazarić
     */
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

    /**
     * Starts the smart script server.
     *
     * @param args the one and only argument is the path to the main configuration file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Program expects one argument: path to main configuration file.");
            return;
        }

        SmartHttpServer server = new SmartHttpServer(args[0]);

        server.start();
    }
}