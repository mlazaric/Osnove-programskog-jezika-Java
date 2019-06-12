package hr.fer.zemris.java.voting.servlets;

import hr.fer.zemris.java.voting.dao.DAOProvider;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Renders a pie chart representing the vote counts for each poll option.
 *
 * @author Marko Lazarić
 */
@WebServlet(urlPatterns = "/servleti/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

    private static final long serialVersionUID = 289564584742693523L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");

        DefaultPieDataset results = new DefaultPieDataset();

        int id = Integer.parseInt(req.getParameter("pollID"));

        DAOProvider.getDao()
                   .getPollOptionsByPollId(id)
                   .stream()
                   .filter(opt -> opt.getVoteCount() > 0)
                   .forEach(opt -> results.setValue(opt.getTitle(), opt.getVoteCount()));

        JFreeChart chart = ChartFactory.createPieChart("Rezultati glasanja", results);

        BufferedImage img = chart.createBufferedImage(500, 500);

        ImageIO.write(img, "png", resp.getOutputStream());
    }
}
