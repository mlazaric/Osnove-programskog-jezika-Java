package hr.fer.zemris.java.voting.servlets;

import hr.fer.zemris.java.voting.dao.DAOProvider;
import hr.fer.zemris.java.voting.model.Poll;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * Shows a list of available polls and links to them.
 *
 * @author Marko Lazarić
 */
@WebServlet(urlPatterns = "/servleti/index.html")
public class GlasanjeIndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Poll> polls = DAOProvider.getDao().listPolls();

        req.setAttribute("polls", polls);

        req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
    }

}
