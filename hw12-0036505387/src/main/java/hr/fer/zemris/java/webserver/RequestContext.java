package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Models the context of a request made by the client to the server.
 *
 * @author Marko Lazarić
 */
public class RequestContext {

    /**
     * The output stream used for writing the response to.
     */
    private OutputStream outputStream;

    /**
     * The charset used for the response.
     */
    private Charset charset = StandardCharsets.UTF_8;

    /**
     * The encoding used for the response.
     */
    private String encoding = "UTF-8";

    /**
     * The status code of the response.
     */
    private int statusCode = 200;

    /**
     * The status text of the response.
     */
    private String statusText = "OK";

    /**
     * The mime type of the response.
     */
    private String mimeType = "text/html";

    /**
     * The content length of the response. Can be null.
     */
    private Long contentLength = null;

    /**
     * The GET parameters of the request.
     */
    private final Map<String, String> parameters;

    /**
     * The temporary parameters of the request.
     */
    private final Map<String, String> temporaryParameters;

    /**
     * The persistent parameters of the request.
     */
    private final Map<String, String> persistentParameters;

    /**
     * The cookies to output.
     */
    private final List<RCCookie> outputCookies;

    /**
     * Whether the header has already been generated.
     */
    private boolean headerGenerated = false;

    /**
     * The object which has created this {@link RequestContext}.
     */
    private IDispatcher dispatcher;

    /**
     * The session ID.
     */
    private final String sid;

    /**
     * Creates a new {@link RequestContext} with the given arguments.
     *
     * @param outputStream the output stream used for writing the response to
     * @param parameters the GET parameters of the request
     * @param persistentParameters the persistent parameters of the request
     * @param outputCookies the cookies to output
     *
     * @throws NullPointerException if {@code outputStream} is {@code null}
     */
    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
        this(outputStream, parameters, persistentParameters, outputCookies, null, null, null);
    }

    /**
     * Creates a new {@link RequestContext} with the given arguments.
     *
     * @param outputStream the output stream used for writing the response to
     * @param parameters the GET parameters of the request
     * @param persistentParameters the persistent parameters of the request
     * @param outputCookies the cookies to output
     * @param temporaryParameters the temporary parameters of the request
     * @param dispatcher the object which has created this {@link RequestContext}
     * @param sid the session ID
     *
     * @throws NullPointerException if {@code outputStream} is {@code null}
     */
    public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies,
                          Map<String, String> temporaryParameters, IDispatcher dispatcher, String sid) {
        this.outputStream = Objects.requireNonNull(outputStream, "Output stream cannot be null.");

        // If any of them are null, treat them as empty
        this.parameters = parameters == null ? new HashMap<>() : parameters;
        this.temporaryParameters = temporaryParameters == null ? new HashMap<>() : temporaryParameters;
        this.persistentParameters = persistentParameters == null ? new HashMap<>() : persistentParameters;
        this.outputCookies = outputCookies == null ? new ArrayList<>() : outputCookies;
        this.dispatcher = dispatcher;
        this.sid = sid;
    }

    /**
     * Returns the GET parameter with the specified name or null.
     *
     * @param name the name of the GET parameter
     * @return the GET parameter with the specified name or null
     */
    public String getParameter(String name) {
        return parameters.get(name);
    }

    /**
     * Returns a set of all the GET parameter names.
     *
     * @return set of all the GET parameter names
     */
    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(parameters.keySet());
    }

    // Persistent parameters

    /**
     * Returns the persistent parameter with the specified name or null.
     *
     * @param name the name of the persistent parameter
     * @return the persistent parameter with the specified name or null
     */
    public String getPersistentParameter(String name) {
        return persistentParameters.get(name);
    }

    /**
     * Returns a set of all the persistent parameter names.
     *
     * @return set of all the persistent parameter names
     */
    public Set<String> getPersistentParameterNames() {
        return Collections.unmodifiableSet(persistentParameters.keySet());
    }

    /**
     * Sets the persistent parameter with the specified name to the specified value.
     *
     * @param name the name of the persistent parameter
     * @param value the new value of the persistent parameter.
     */
    public void setPersistentParameter(String name, String value) {
        persistentParameters.put(name, value);
    }

    /**
     * Deletes the persistent parameter with the specified name.
     *
     * @param name the name of the persistent parameter to delete
     */
    public void removePersistentParameter(String name) {
        persistentParameters.remove(name);
    }

    // Temporary parameters

    /**
     * Returns the temporary parameter with the specified name or null.
     *
     * @param name the name of the temporary parameter
     * @return the temporary parameter with the specified name or null
     */
    public String getTemporaryParameter(String name) {
        return temporaryParameters.get(name);
    }

    /**
     * Returns a set of all the temporary parameter names.
     *
     * @return set of all the temporary parameter names
     */
    public Set<String> getTemporaryParameterNames() {
        return Collections.unmodifiableSet(temporaryParameters.keySet());
    }

    /**
     * Sets the temporary parameter with the specified name to the specified value.
     *
     * @param name the name of the temporary parameter
     * @param value the new value of the temporary parameter.
     */
    public void setTemporaryParameter(String name, String value) {
        temporaryParameters.put(name, value);
    }

    /**
     * Deletes the temporary parameter with the specified name.
     *
     * @param name the name of the temporary parameter to delete
     */
    public void removeTemporaryParameter(String name) {
        temporaryParameters.remove(name);
    }

    /**
     * Returns the session ID.
     *
     * @return the session ID
     */
    public String getSessionID() {
        return sid;
    }

    /**
     * Helper function which throws a {@link RuntimeException} if the header has already been generated.
     */
    private void throwIfHeaderAlreadyGenerated() {
        if (headerGenerated) {
            throw new RuntimeException("Header is already generated, cannot modify it.");
        }
    }

    /**
     * Sets the encoding to the new value.
     *
     * @param encoding the new value of the encoding
     *
     * @throws RuntimeException if the header has already been generated
     */
    public void setEncoding(String encoding) {
        throwIfHeaderAlreadyGenerated();

        this.charset = Charset.forName(encoding);
        this.encoding = encoding;
    }

    /**
     * Sets the status code to the new value.
     *
     * @param statusCode the new value of the status code
     *
     * @throws RuntimeException if the header has already been generated
     */
    public void setStatusCode(int statusCode) {
        throwIfHeaderAlreadyGenerated();

        this.statusCode = statusCode;
    }

    /**
     * Sets the status text to the new value.
     *
     * @param statusText the new value of the status text
     *
     * @throws RuntimeException if the header has already been generated
     */
    public void setStatusText(String statusText) {
        throwIfHeaderAlreadyGenerated();

        this.statusText = statusText;
    }

    /**
     * Sets the mime type to the new value.
     *
     * @param mimeType the new value of the mime type
     *
     * @throws RuntimeException if the header has already been generated
     */
    public void setMimeType(String mimeType) {
        throwIfHeaderAlreadyGenerated();

        this.mimeType = mimeType;
    }

    /**
     * Sets the content length to the new value.
     *
     * @param contentLength the new value of the content length
     *
     * @throws RuntimeException if the header has already been generated
     */
    public void setContentLength(Long contentLength) {
        throwIfHeaderAlreadyGenerated();

        this.contentLength = contentLength;
    }

    /**
     * Adds a cookie to the output cookies.
     *
     * @param rcCookie the cookie to add to output cookies
     *
     * @throws RuntimeException if the header has already been generated
     */
    public void addRCCookie(RCCookie rcCookie) {
        throwIfHeaderAlreadyGenerated();

        outputCookies.add(rcCookie);
    }

    /**
     * Writes the byte array to the output stream.
     *
     * @param data the data to write
     * @return this object
     *
     * @throws IOException if an error occurs while writing to the output stream
     */
    public RequestContext write(byte[] data) throws IOException {
        return write(data, 0, data.length);
    }

    /**
     * Writes the String to the output stream using the {@link #charset}.
     *
     * @param text the text to write
     * @return this object
     *
     * @throws IOException if an error occurs while writing to the output stream
     */
    public RequestContext write(String text) throws IOException {
        return write(text.getBytes(charset));
    }

    /**
     * Writes the byte array from offset to offset+len to the output stream.
     *
     * @param data the data to write
     * @param offset the offset of the first byte to write
     * @param len the number of bytes to write from the array
     * @return this object
     *
     * @throws IOException if an error occurs while writing to the output stream
     */
    public RequestContext write(byte[] data, int offset, int len) throws IOException {
        if (!headerGenerated) {
            generateHeader();
        }

        outputStream.write(data, offset, len);

        return this;
    }

    /**
     * Generates the header and outputs it to the output stream.
     *
     * @throws IOException if an error occurs while writing to the output stream
     */
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

    /**
     * Returns the properly formatted mime type. Adds charset information if the mime type is a text type.
     *
     * @return the properly formatted mime type
     */
    private String getMimeType() {
        if (mimeType.toLowerCase().startsWith("text/")) {
            return mimeType + "; charset=" + encoding;
        }

        return mimeType;
    }

    /**
     * Returns the dispatcher which has created this one.
     *
     * @return the dispatcher which has created this one
     */
    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * Models a single request context cookie.
     *
     * @author Marko Lazarić
     */
    public static class RCCookie {

        /**
         * The name of the cookie.
         */
        private final String name;

        /**
         * The value of the cookie.
         */
        private final String value;

        /**
         * The domain of the cookie.
         */
        private final String domain;

        /**
         * The path of the cookie.
         */
        private final String path;

        /**
         * The maximum age of the cookie.
         */
        private final Integer maxAge;

        /**
         * Whether it is http only or not.
         */
        private final boolean httpOnly;

        /**
         * Creates an {@link RCCookie} with the given arguments.
         *
         * @param name the name of the cookie
         * @param value the value of the cookie
         * @param maxAge the maximum age of the cookie
         * @param domain the domain of the cookie
         * @param path the path of the cookie
         */
        public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
            this(name, value, maxAge, domain, path, false);
        }

        /**
         * Creates an {@link RCCookie} with the given arguments.
         *
         * @param name the name of the cookie
         * @param value the value of the cookie
         * @param maxAge the maximum age of the cookie
         * @param domain the domain of the cookie
         * @param path the path of the cookie
         * @param httpOnly whether it is http only or not
         */
        public RCCookie(String name, String value, Integer maxAge, String domain, String path, boolean httpOnly) {
            this.name = name;
            this.value = value;
            this.domain = domain;
            this.path = path;
            this.maxAge = maxAge;
            this.httpOnly = httpOnly;
        }

        /**
         * Returns the name of the cookie.
         *
         * @return the name of the cookie
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the value of the cookie.
         *
         * @return the value of the cookie
         */
        public String getValue() {
            return value;
        }

        /**
         * Returns the domain of the cookie.
         *
         * @return the domain of the cookie
         */
        public String getDomain() {
            return domain;
        }

        /**
         * Returns the path of the cookie.
         *
         * @return the path of the cookie
         */
        public String getPath() {
            return path;
        }

        /**
         * Returns the maximum age of the cookie.
         *
         * @return the maximum age of the cookie
         */
        public Integer getMaxAge() {
            return maxAge;
        }

        /**
         * Returns whether the cookie is http only.
         *
         * @return whether the cookie is http only
         */
        public boolean isHttpOnly() {
            return httpOnly;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("Set-Cookie: ");

            sb.append(name).append("=\"").append(value).append("\"; ");
            appendIfNotNull(sb, "Domain", domain);
            appendIfNotNull(sb, "Path", path);
            appendIfNotNull(sb, "Max-Age", maxAge == null ? null : maxAge.toString());

            if (httpOnly) {
                sb.append("HttpOnly; ");
            }

            // Remove "; "
            sb.delete(sb.length() - 2, sb.length());

            return sb.toString();
        }

        /**
         * Appends "name=value; " if value is not null.
         *
         * @param sb the {@link StringBuilder} to append to
         * @param name the name to append
         * @param value the value to append
         */
        private void appendIfNotNull(StringBuilder sb, String name, String value) {
            if (value != null) {
                sb.append(name).append("=").append(value).append("; ");
            }
        }
    }

}
