package hr.fer.zemris.java.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Sets the "pickedBgCol" session attribute to the value of the "pickedBgCol" GET parameter.
 *
 * @author Marko Lazarić
 */
@WebServlet(name = "setcolor", urlPatterns = { "/setcolor" })
public class SetColorServlet extends HttpServlet {

    private static final long serialVersionUID = -7162095442912141335L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pickedBgCol = req.getParameter("pickedBgCol");

        if (pickedBgCol == null) {
            resp.sendRedirect(req.getContextPath() + "/colors.jsp");
        }

        req.getSession().setAttribute("pickedBgCol", pickedBgCol);
        resp.sendRedirect(req.getContextPath() + "/colors.jsp");
    }

}
