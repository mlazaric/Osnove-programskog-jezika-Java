package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RequestContext {

    private OutputStream outputStream;
    private Charset charset = StandardCharsets.UTF_8;

    private String encoding = "UTF-8";
    private int statusCode = 200;
    private String statusText = "OK";
    private String mimeType = "text/html";

    private Long contentLength = null;

    private final Map<String, String> parameters;
    private final Map<String, String> temporaryParameters;
    private final Map<String, String> persistentParameters;
    private final List<RCCookie> outputCookies;
    private boolean headerGenerated = false;

    private IDispatcher dispatcher;

    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
        this(outputStream, parameters, persistentParameters, outputCookies, null, null);
    }

    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies,
                          Map<String, String> temporaryParameters, IDispatcher dispatcher) {
        this.outputStream = Objects.requireNonNull(outputStream, "Output stream cannot be null.");

        // If any of them are null, treat them as empty
        this.parameters = parameters == null ? new HashMap<>() : parameters;
        this.temporaryParameters = temporaryParameters == null ? new HashMap<>() : temporaryParameters;
        this.persistentParameters = persistentParameters == null ? new HashMap<>() : persistentParameters;
        this.outputCookies = outputCookies == null ? new ArrayList<>() : outputCookies;
        this.dispatcher = dispatcher;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(parameters.keySet());
    }

    // Persistent parameters

    public String getPersistentParameter(String name) {
        return persistentParameters.get(name);
    }

    public Set<String> getPersistentParameterNames() {
        return Collections.unmodifiableSet(persistentParameters.keySet());
    }

    public void setPersistentParameter(String name, String value) {
        persistentParameters.put(name, value);
    }

    public void removePersistentParameter(String name) {
        persistentParameters.remove(name);
    }

    // Temporary parameters

    public String getTemporaryParameter(String name) {
        return temporaryParameters.get(name);
    }

    public Set<String> getTemporaryParameterNames() {
        return Collections.unmodifiableSet(temporaryParameters.keySet());
    }

    public void setTemporaryParameter(String name, String value) {
        temporaryParameters.put(name, value);
    }

    public void removeTemporaryParameter(String name) {
        temporaryParameters.remove(name);
    }

    public String getSessionID() {
        return "";
    }

    private void throwIfHeaderAlreadyGenerated() {
        if (headerGenerated) {
            throw new RuntimeException("Header is already generated, cannot modify it.");
        }
    }

    public void setEncoding(String encoding) {
        throwIfHeaderAlreadyGenerated();

        this.charset = Charset.forName(encoding);
        this.encoding = encoding;
    }

    public void setStatusCode(int statusCode) {
        throwIfHeaderAlreadyGenerated();

        this.statusCode = statusCode;
    }

    public void setStatusText(String statusText) {
        throwIfHeaderAlreadyGenerated();

        this.statusText = statusText;
    }

    public void setMimeType(String mimeType) {
        throwIfHeaderAlreadyGenerated();

        this.mimeType = mimeType;
    }

    public void setContentLength(Long contentLength) {
        throwIfHeaderAlreadyGenerated();

        this.contentLength = contentLength;
    }

    public void addRCCookie(RCCookie rcCookie) {
        throwIfHeaderAlreadyGenerated();

        outputCookies.add(rcCookie);
    }

    public RequestContext write(byte[] data) throws IOException {
        return write(data, 0, data.length);
    }

    public RequestContext write(String text) throws IOException {
        return write(text.getBytes(charset));
    }

    public RequestContext write(byte[] data, int offset, int len) throws IOException {
        if (!headerGenerated) {
            generateHeader();
        }

        outputStream.write(data, offset, len);

        return this;
    }

    private void generateHeader() throws IOException {
        headerGenerated = true;

        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusText).append("\r\n");
        sb.append("Content-Type: ").append(getMimeType()).append("\r\n");

        if (contentLength != null) {
            sb.append("Content-Length: ").append(contentLength).append("\r\n");
        }

        for (RCCookie cookie : outputCookies) {
            sb.append(cookie).append("\r\n");
        }
        
        sb.append("Connection: close\r\n");

        sb.append("\r\n");

        outputStream.write(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
    }

    private String getMimeType() {
        if (mimeType.toLowerCase().startsWith("text/")) {
            return mimeType + "; charset=" + encoding;
        }

        return mimeType;
    }

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    public static class RCCookie {
        private final String name;
        private final String value;
        private final String domain;
        private final String path;
        private final Integer maxAge;

        public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
            this.name = name;
            this.value = value;
            this.domain = domain;
            this.path = path;
            this.maxAge = maxAge;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getDomain() {
            return domain;
        }

        public String getPath() {
            return path;
        }

        public Integer getMaxAge() {
            return maxAge;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("Set-Cookie: ");

            sb.append(name).append("=\"").append(value).append("\"; ");
            appendIfNotNull(sb, "Domain", domain);
            appendIfNotNull(sb, "Path", path);
            appendIfNotNull(sb, "Max-Age", maxAge == null ? null : maxAge.toString());

            // Remove "; "
            sb.delete(sb.length() - 2, sb.length());

            return sb.toString();
        }

        private void appendIfNotNull(StringBuilder sb, String name, String value) {
            if (value != null) {
                sb.append(name).append("=").append(value).append("; ");
            }
        }
    }

}
