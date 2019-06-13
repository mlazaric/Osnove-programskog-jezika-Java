package hr.fer.zemris.java.web.voting;

import hr.fer.zemris.java.voting.IBandDefinitionStorage;
import hr.fer.zemris.java.voting.IBandVotesStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Shows the results of the voting. Displays the number of votes each band received, a pie chart representation of the
 * votes, a link to an excel spreadsheet of the votes and the bands with the most votes.
 *
 * @author Marko Lazarić
 */
@WebServlet(name = "glasanje-rezultati", urlPatterns = { "/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {

    private static final long serialVersionUID = -4901873920085701861L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IBandVotesStorage votes = (IBandVotesStorage) req.getAttribute("votes");
        IBandDefinitionStorage bandDefinition = (IBandDefinitionStorage) req.getAttribute("bandDefinition");

        req.setAttribute("bandVoteCounts", votes.sortedByVoteCount(bandDefinition).iterator());
        req.setAttribute("bestVoted", votes.bestVoted(bandDefinition).iterator());

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }
}
