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

    private int getIntOrDefault(Object object, int defaultValue) {
        try {
            return Integer.parseInt((String) object);
        }
        catch (NumberFormatException ignored) {}

        return defaultValue;
    }

    public static class TrigonometricValue {

        private final double angle;
        private final double sin;
        private final double cos;

        private TrigonometricValue(double angle) {
            this.angle = angle;
            this.sin = sin(toRadians(angle));
            this.cos = cos(toRadians(angle));
        }

        public double getAngle() {
            return angle;
        }

        public double getSin() {
            return sin;
        }

        public double getCos() {
            return cos;
        }
    }

}
