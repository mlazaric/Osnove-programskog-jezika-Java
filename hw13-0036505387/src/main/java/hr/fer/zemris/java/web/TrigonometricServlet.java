package hr.fer.zemris.java.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

/**
 * Renders a table of values of sine and cosine from a (default 0) to b (default 360) in degrees (both inclusive).
 *
 * @author Marko Lazarić
 */
@WebServlet(name = "trigonometric", urlPatterns = { "/trigonometric" })
public class TrigonometricServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int a = getIntOrDefault(req.getParameter("a"), 0);
        int b = getIntOrDefault(req.getParameter("b"), 360);

        if (a > b) {
            int c = a;
            a = b;
            b = c;
        }

        if (b > a + 720) {
            b = a + 720;
        }

        List<TrigonometricValue> values = new ArrayList<>();

        for (int angle = a; angle <= b; ++angle) {
            values.add(new TrigonometricValue(angle));
        }

        req.setAttribute("values", values);
        req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
    }

    /**
     * Try to parse the object as an int, if it is not parsable, return the default value.
     *
     * @param object the object to try to parse as an int
     * @param defaultValue the default value to return the object is not parsable
     * @return the parsed int or the default value if the object could not be parsed
     */
    private int getIntOrDefault(Object object, int defaultValue) {
        try {
            return Integer.parseInt((String) object);
        }
        catch (NumberFormatException ignored) {}

        return defaultValue;
    }

    /**
     * A java bean class which represents an angle and the values of sine and cosine for that angle.
     *
     * @author Marko Lazarić
     */
    public static class TrigonometricValue {

        /**
         * The angle used for calculating the sine and cosine values.
         */
        private final double angle;

        /**
         * The value of the sine function for the specified angle.
         */
        private final double sin;

        /**
         * The value of the cosine function for the specified angle.
         */
        private final double cos;

        /**
         * Creates a new {@link TrigonometricValue} with the given argument.
         *
         * @param angle the angle used for calculating the sine and cosine values
         */
        private TrigonometricValue(double angle) {
            this.angle = angle;
            this.sin = sin(toRadians(angle));
            this.cos = cos(toRadians(angle));
        }

        /**
         * Returns the angle used for calculating the sine and cosine values.
         *
         * @return the angle used for calculating the sine and cosine values
         */
        public double getAngle() {
            return angle;
        }

        /**
         * Returns the value of the sine function for the specified angle.
         *
         * @return the value of the sine function for the specified angle
         */
        public double getSin() {
            return sin;
        }

        /**
         * Returns the value of the cosine function for the specified angle.
         *
         * @return the value of the cosine function for the specified angle
         */
        public double getCos() {
            return cos;
        }
    }

}
