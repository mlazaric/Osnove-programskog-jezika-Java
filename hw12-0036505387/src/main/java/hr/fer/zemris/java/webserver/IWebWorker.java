package hr.fer.zemris.java.webserver;

public interface IWebWorker {
    void processRequest(RequestContext context) throws Exception;
}