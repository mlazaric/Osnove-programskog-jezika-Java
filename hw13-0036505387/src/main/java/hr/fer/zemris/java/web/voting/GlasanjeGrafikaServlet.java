package hr.fer.zemris.java.web.voting;

import hr.fer.zemris.java.voting.IBandDefinitionStorage;
import hr.fer.zemris.java.voting.IBandVotesStorage;
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
 * Renders a pie chart representing the vote counts for each band.
 *
 * @author Marko Lazarić
 */
@WebServlet(name = "glasanje-grafika", urlPatterns = { "/glasanje-grafika" })
public class GlasanjeGrafikaServlet extends HttpServlet {

    private static final long serialVersionUID = 6547413337481299718L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IBandVotesStorage votes = (IBandVotesStorage) req.getAttribute("votes");
        IBandDefinitionStorage bandDefinition = (IBandDefinitionStorage) req.getAttribute("bandDefinition");

        resp.setContentType("image/png");

        DefaultPieDataset results = new DefaultPieDataset();

        votes.sortedByVoteCount(bandDefinition)
             .forEach(bvc -> {
                 if (bvc.getVoteCount() > 0) {
                     results.setValue(bvc.getBand().getName(), bvc.getVoteCount());
                 }
             });

        JFreeChart chart = ChartFactory.createPieChart("Rezultati glasanja", results);

        BufferedImage img = chart.createBufferedImage(500, 500);

        ImageIO.write(img, "png", resp.getOutputStream());
    }
}
