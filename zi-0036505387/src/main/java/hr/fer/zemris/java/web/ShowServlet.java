package hr.fer.zemris.java.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static hr.fer.zemris.java.web.IndexServlet.JVD_DIR;

@WebServlet(urlPatterns = "/show")
public class ShowServlet extends HttpServlet {

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

        AtomicInteger lines = new AtomicInteger();
        AtomicInteger circles = new AtomicInteger();
        AtomicInteger filledCircles = new AtomicInteger();
        AtomicInteger filledTriangles = new AtomicInteger();

        Files.lines(filePath)
                .forEach(l -> {
                    if (l.startsWith("LINE")) lines.incrementAndGet();
                    if (l.startsWith("CIRCLE")) circles.incrementAndGet();
                    if (l.startsWith("FCIRCLE")) filledCircles.incrementAndGet();
                    if (l.startsWith("FTRIANGLE")) filledTriangles.incrementAndGet();
                });


        req.setAttribute("name", name);
        req.setAttribute("numberOfLines", lines.get());
        req.setAttribute("numberOfCircles", circles.get());
        req.setAttribute("numberOfFilledCircles", filledCircles.get());
        req.setAttribute("numberOfFilledTriangles", filledTriangles.get());

        req.getRequestDispatcher("/WEB-INF/pages/show.jsp")
                .forward(req, resp);
    }

}
