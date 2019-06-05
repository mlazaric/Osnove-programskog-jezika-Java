package hr.fer.zemris.java.web;

import hr.fer.zemris.java.web.voting.IBandVotesStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "glasanje-glasaj", urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {

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
