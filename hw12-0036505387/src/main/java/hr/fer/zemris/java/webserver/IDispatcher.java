package hr.fer.zemris.java.webserver;

/**
 * Models a simple request dispatcher. Used to implement MVC.
 *
 * @author Marko Lazarić
 */
public interface IDispatcher {

    /**
     * Dispatch a request to the requested url.
     *
     * @param urlPath the url to which to dispatch a request to
     * @throws Exception an exception is thrown if the dispatching fails unexpectedly
     */
    void dispatchRequest(String urlPath) throws Exception;

}