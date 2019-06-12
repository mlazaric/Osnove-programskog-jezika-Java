package hr.fer.zemris.java.voting.servlets;

import hr.fer.zemris.java.voting.dao.DAOProvider;
import hr.fer.zemris.java.voting.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Shows the results of the voting. Displays the number of votes each band received, a pie chart representation of the
 * votes, a link to an excel spreadsheet of the votes and the bands with the most votes.
 *
 * @author Marko Lazarić
 */
@WebServlet(urlPatterns = "/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        List<PollOption> options = DAOProvider.getDao().getPollOptionsByPollId(id);
        List<PollOption> bestVoted = DAOProvider.getDao().getBestVotedOptions(id);

        req.setAttribute("options", options);
        req.setAttribute("bestVoted", bestVoted);
        req.setAttribute("id", id);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }
}
