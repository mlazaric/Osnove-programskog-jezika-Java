package hr.fer.zemris.java.webserver;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmartHttpServer {

    private String address;
    private String domainName;
    private int port;
    private int workerThreads;
    private int sessionTimeout;
    private Map<String, String> mimeTypes = new HashMap<String, String>();
    private ServerThread serverThread;
    private ExecutorService threadPool;
    private Path documentRoot;

    public SmartHttpServer(String configFileName) {
        loadServerConfiguration(configFileName);

        serverThread = new ServerThread();
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
    private class ClientWorker implements Runnable {
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

        public ClientWorker(Socket csocket) {
            super();
            this.csocket = csocket;
        }

        @Override
        public void run() {
            try {
                unsafelyRun();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void unsafelyRun() throws IOException {
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

            parseParameters(paramString);

            Path requestedFilePath = documentRoot.resolve(Paths.get(path.substring(1)));

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
                int index = path.lastIndexOf('.');

                if (index != -1) {
                    String extension = path.substring(index + 1);

                    mime = mimeTypes.get(extension);
                }
            }

            if (mime == null) {
                mime = "application/octet-stream";
            }

            RequestContext rc = new RequestContext(ostream, params, permPrams, outputCookies);

            rc.setMimeType(mime);
            rc.setContentLength(Files.size(requestedFilePath));
            rc.write(Files.readAllBytes(requestedFilePath));
            ostream.flush();
            csocket.close();

            // If you want, you can modify RequestContext to allow you to add additional headers
            // so that you can add “Content-Length: 12345” if you know that file has 12345 bytes
            // open file, read its content and write it to rc (that will generate header and send
            // file bytes to client)
        }

        private void returnError(int statusCode, String statusText) throws IOException {
            RequestContext rc = new RequestContext(ostream, null, null, null);

            rc.setStatusCode(statusCode);
            rc.setStatusText(statusText);

            rc.write("");
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