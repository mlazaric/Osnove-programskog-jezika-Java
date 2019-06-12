package hr.fer.zemris.java.voting.servlets;

import hr.fer.zemris.java.voting.dao.DAOProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Adds a single vote to the poll option with the specified id (GET parameter "id") and redirects the user to the results of
 * the voting.
 *
 * @author Marko Lazarić
 */
@WebServlet(urlPatterns = "/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

    private static final long serialVersionUID = 515880567442012425L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") == null) {
            req.setAttribute("message", "ID of band is not set.");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp")
               .forward(req, resp);
            return;
        }

        int id = Integer.parseInt(req.getParameter("id"));

        DAOProvider.getDao().voteFor(id);

        int pollID = DAOProvider.getDao()
                                .getPollOptionById(id)
                                .getPollId();

        resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + pollID);
    }

}
