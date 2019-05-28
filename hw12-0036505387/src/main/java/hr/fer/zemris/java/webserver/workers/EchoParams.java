package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EchoParams implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) {
        context.setMimeType("text/html");
        try {
            context.write("<html><body><table>");

            context.write("<tr>");
            context.write("<td>Parameter name</td>");
            context.write("<td>Parameter value</td>");
            context.write("</tr>");

            for (String parameterName : context.getParameterNames()) {
                String value = context.getParameter(parameterName);

                context.write("<tr>");
                context.write("<td>" + parameterName + "</td>");
                context.write("<td>" + value + "</td>");
                context.write("</tr>");
            }

            context.write("</table></body></html>");
        } catch (IOException ex) {
            // Log exception to servers log...
            ex.printStackTrace();
        }
    }

}