package hr.fer.zemris.java.webserver;

/**
 * Models a simple web worker which can process requests.
 *
 * @author Marko Lazarić
 */
public interface IWebWorker {

    /**
     * Processes a request and sends a response.
     *
     * @param context the context of the request to process
     * @throws Exception an exception is thrown if an unexpected error is encountered while processing the request
     */
    void processRequest(RequestContext context) throws Exception;
}