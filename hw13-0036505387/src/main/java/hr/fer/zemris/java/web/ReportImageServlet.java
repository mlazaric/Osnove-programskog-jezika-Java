package hr.fer.zemris.java.web;

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
 * Renders a pie chart of the reported OS usage.
 *
 * @author Marko Lazarić
 */
@WebServlet(name = "reportImage", urlPatterns = { "/reportImage" })
public class ReportImageServlet extends HttpServlet {

    private static final long serialVersionUID = -1431705454028307360L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");

        DefaultPieDataset results = new DefaultPieDataset();

        results.setValue("Linux", 10);
        results.setValue("Mac", 5);
        results.setValue("Windows", 85);

        JFreeChart chart = ChartFactory.createPieChart("OS Usage", results);

        BufferedImage img = chart.createBufferedImage(500, 500);

        ImageIO.write(img, "png", resp.getOutputStream());
    }

}
