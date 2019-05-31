package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Renders a HTML document with links to some of the scripts and workers.
 *
 * @author Marko Lazarić
 */
public class HomeWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) {
        String bgcolor = context.getPersistentParameter("bgcolor");

        if (bgcolor == null) {
            bgcolor = "7F7F7F";
        }

        context.setTemporaryParameter("background", bgcolor);
        
        try {
            context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}