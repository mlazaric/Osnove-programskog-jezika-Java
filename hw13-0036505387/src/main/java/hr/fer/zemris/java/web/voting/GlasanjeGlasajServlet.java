package hr.fer.zemris.java.web.voting;

import hr.fer.zemris.java.voting.IBandVotesStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Adds a single vote to the band with the specified id (GET parameter "id") and redirects the user to the results of
 * the voting.
 *
 * @author Marko Lazarić
 */
@WebServlet(name = "glasanje-glasaj", urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {

    private static final long serialVersionUID = 8844538846898330603L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IBandVotesStorage votes = (IBandVotesStorage) req.getAttribute("votes");

        if (req.getParameter("id") == null) {
            req.setAttribute("message", "ID of band is not set.");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp")
               .forward(req, resp);
            return;
        }

        int id = Integer.parseInt(req.getParameter("id"));

        votes.voteFor(id);

        resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
    }

}
