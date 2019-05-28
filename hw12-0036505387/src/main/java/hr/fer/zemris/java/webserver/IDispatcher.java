package hr.fer.zemris.java.webserver;

public interface IDispatcher {
    void dispatchRequest(String urlPath) throws Exception;
}