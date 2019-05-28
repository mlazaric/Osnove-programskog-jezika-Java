package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SumWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) {
        int a = parseOrDefault(context.getParameter("a"), 1);
        int b = parseOrDefault(context.getParameter("b"), 2);
        int sum = a + b;
        String imgName = "even.png";

        if ((sum % 2) == 1) {
            imgName = "odd.png";
        }

        context.setTemporaryParameter("varA", a + "");
        context.setTemporaryParameter("varB", b + "");
        context.setTemporaryParameter("zbroj", sum + "");
        context.setTemporaryParameter("imgName", imgName);

        try {
            context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int parseOrDefault(String toParse, int defaultValue) {
        try {
            return Integer.parseInt(toParse);
        }
        catch (NumberFormatException ignored) {}

        return defaultValue;
    }

}