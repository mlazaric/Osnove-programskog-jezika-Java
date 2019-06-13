package hr.fer.zemris.java.voting.servlets;

import hr.fer.zemris.java.voting.dao.DAOProvider;
import hr.fer.zemris.java.voting.model.Poll;
import hr.fer.zemris.java.voting.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Shows a list of poll options the users can vote for with links to vote for those options.
 *
 * @author Marko Lazarić
 */
@WebServlet(urlPatterns = "/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("pollID"));

        Poll poll = DAOProvider.getDao().getPollById(id);
        List<PollOption> options = DAOProvider.getDao().getPollOptionsByPollId(id);

        req.setAttribute("poll", poll);
        req.setAttribute("options", options);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
    }

}
