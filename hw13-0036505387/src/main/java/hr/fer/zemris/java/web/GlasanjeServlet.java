package hr.fer.zemris.java.web;

import hr.fer.zemris.java.web.voting.IBandDefinitionStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Shows a list of bands the users can vote for with links to vote for those bands.
 *
 * @author Marko Lazarić
 */
@WebServlet(name = "glasanje", urlPatterns = { "/glasanje" })
public class GlasanjeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IBandDefinitionStorage bandDefinition = (IBandDefinitionStorage) req.getAttribute("bandDefinition");

        req.setAttribute("bands", bandDefinition.iterator());

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
    }

}
