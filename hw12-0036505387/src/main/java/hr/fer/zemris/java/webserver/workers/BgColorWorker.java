package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.awt.*;

/**
 * Sets the "bgcolor" persistent parameter to the value of the "bgcolor" parameter if it is set and it represents a
 * valid color and outputs an HTML document.
 *
 * @author Marko Lazarić
 */
public class BgColorWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) {
        String bgcolor = context.getParameter("bgcolor");

        if (bgcolor == null) {
            context.setTemporaryParameter("message", "Background color was not updated.");
        }
        else {
            try {
                Color.decode("#" + bgcolor);

                context.setTemporaryParameter("message", "Background color was updated.");
                context.setPersistentParameter("bgcolor", bgcolor);
            }
            catch (NumberFormatException e) {
                context.setTemporaryParameter("message", "Background color was not updated.");
            }
        }

        try {
            context.getDispatcher().dispatchRequest("/private/pages/bgcolor.smscr");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}