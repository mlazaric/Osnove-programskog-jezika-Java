package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Sums two numbers parsed as GET parameters, uses default values if either or both are not set.
 *
 * @author Marko Lazarić
 */
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

    /**
     * Tries to parse the {@link String} as a number, if it is not parsable, it returns the default value.
     *
     * @param toParse the {@link String} to parse
     * @param defaultValue the default value
     * @return the parsed number or default value
     */
    private int parseOrDefault(String toParse, int defaultValue) {
        try {
            return Integer.parseInt(toParse);
        }
        catch (NumberFormatException ignored) {}

        return defaultValue;
    }

}