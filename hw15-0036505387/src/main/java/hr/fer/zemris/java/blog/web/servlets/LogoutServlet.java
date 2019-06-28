package hr.fer.zemris.java.blog.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet which logs the {@link hr.fer.zemris.java.blog.model.BlogUser} out.
 *
 * @author Marko Lazarić
 */
@WebServlet(urlPatterns = "/servleti/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1765990960756258255L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();

        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }

}
