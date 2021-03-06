package hr.fer.zemris.java.voting.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Redirects /index.html to /servleti/index.html.
 *
 * @author Marko Lazarić
 */
@WebServlet(urlPatterns = "/index.html")
public class IndexRedirectionServlet extends HttpServlet {

    private static final long serialVersionUID = -2570281105677983133L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(getServletContext().getContextPath() + "/servleti/index.html");
    }

}
