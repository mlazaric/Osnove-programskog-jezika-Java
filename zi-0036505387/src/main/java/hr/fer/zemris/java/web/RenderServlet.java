package hr.fer.zemris.java.web;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectPainter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static hr.fer.zemris.java.hw17.jvdraw.JVDraw.*;
import static hr.fer.zemris.java.web.IndexServlet.JVD_DIR;

@WebServlet(urlPatterns = "/render")
public class RenderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("image");

        if (name == null || name.isEmpty()) {
            req.setAttribute("message", "Null parameters...");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp")
                    .forward(req, resp);
            return;
        }

        Path imagesPath = Paths.get(req.getServletContext().getRealPath(JVD_DIR));
        Path filePath = imagesPath.resolve("./" + name);

        if (Files.notExists(filePath)) {
            resp.sendError(404);
            return;
        }

        List<GeometricalObject> model;

        try {
            model = parseFile(filePath);
        }
        catch (RuntimeException e) {
            req.setAttribute("message", "Could not parse " + name);
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp")
                    .forward(req, resp);
            return;
        }

        GeometricalObjectBBCalculator boundingBoxCalc = new GeometricalObjectBBCalculator();

        for (GeometricalObject object : model) {
            object.accept(boundingBoxCalc);
        }

        Rectangle boundingBox = boundingBoxCalc.getBoundingBox();

        BufferedImage image = new BufferedImage(
                boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = image.createGraphics();

        g.translate(-boundingBox.x, -boundingBox.y);

        GeometricalObjectPainter painter = new GeometricalObjectPainter(g);

        for (GeometricalObject object : model) {
            object.accept(painter);
        }

        g.dispose();

        try {
            resp.setContentType("image/png");
            ImageIO.write(image, "png", resp.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses a JVD file and adds the parsed {@link GeometricalObject}s to the model.
     *
     * @param file the file to parse
     */
    private List<GeometricalObject> parseFile(Path file) {
        try {
            return Files.lines(file, StandardCharsets.UTF_8)
                    .filter(l -> !l.isEmpty())
                    .map(this::parseLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses a line and returns the parsed {@link GeometricalObject}.
     *
     * @param line the line to parse
     * @return the parsed {@link GeometricalObject}
     *
     * @throws RuntimeException if the line is not a parsable {@link GeometricalObject}
     */
    private GeometricalObject parseLine(String line) {
        line = line.strip();

        Matcher matcher = LINE_REGEX.matcher(line);

        if (matcher.matches()) {
            return new Line(
                    new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                    new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))),
                    new Color(Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)), Integer.parseInt(matcher.group(7)))
            );
        }

        matcher = CIRCLE_REGEX.matcher(line);

        if (matcher.matches()) {
            return new Circle(
                    new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                    Integer.parseInt(matcher.group(3)),
                    new Color(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)))
            );
        }

        matcher = FILLED_CIRCLE_REGEX.matcher(line);

        if (matcher.matches()) {
            return new FilledCircle(
                    new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                    Integer.parseInt(matcher.group(3)),
                    new Color(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6))),
                    new Color(Integer.parseInt(matcher.group(7)), Integer.parseInt(matcher.group(8)), Integer.parseInt(matcher.group(9)))
            );
        }

        matcher = FILLED_TRIANGLE_REGEX.matcher(line);

        if (matcher.matches()) {
            return new FilledTriangle(
                    new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                    new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))),
                    new Point(Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6))),
                    new Color(Integer.parseInt(matcher.group(7)), Integer.parseInt(matcher.group(8)), Integer.parseInt(matcher.group(9))),
                    new Color(Integer.parseInt(matcher.group(10)), Integer.parseInt(matcher.group(11)), Integer.parseInt(matcher.group(12)))
            );
        }

        throw new RuntimeException("'" + line + "' is not parsable.");
    }

}
