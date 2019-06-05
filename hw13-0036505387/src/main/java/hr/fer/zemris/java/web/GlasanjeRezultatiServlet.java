package hr.fer.zemris.java.web;

import hr.fer.zemris.java.web.voting.IBandDefinitionStorage;
import hr.fer.zemris.java.web.voting.IBandVotesStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "glasanje-rezultati", urlPatterns = { "/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IBandVotesStorage votes = (IBandVotesStorage) req.getAttribute("votes");
        IBandDefinitionStorage bandDefinition = (IBandDefinitionStorage) req.getAttribute("bandDefinition");

        req.setAttribute("bandVoteCounts", votes.sortedByVoteCount(bandDefinition).iterator());
        req.setAttribute("bestVoted", votes.bestVoted(bandDefinition).iterator());

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }
}
