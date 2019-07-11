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
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/main")
public class IndexServlet extends HttpServlet {

    public static String JVD_DIR = "WEB-INF/images";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Path imagesPath = Paths.get(req.getServletContext().getRealPath(JVD_DIR));
        List<String> images;

        if (Files.notExists(imagesPath) || !Files.isDirectory(imagesPath)) {
            images = Collections.emptyList();
        }
        else {
            images = Files.walk(imagesPath)
                    .map(p -> p.getFileName().toString())
                    .filter(s -> s.endsWith(".jvd"))
                    .sorted()
                    .collect(Collectors.toList());
        }

        req.setAttribute("images", images);
        req.getRequestDispatcher("/WEB-INF/pages/list.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String contents = req.getParameter("contents");

        if (name == null || contents == null || name.isEmpty() || contents.isEmpty()) {
            req.setAttribute("message", "Null parameters...");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp")
                    .forward(req, resp);
            return;
        }

        if (!name.matches("[\\.A-Za-z0-9]+") || !name.endsWith(".jvd")) {
            req.setAttribute("message", "Invalid name '" + name + "'.");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp")
                    .forward(req, resp);
            return;
        }

        Path imagesPath = Paths.get(req.getServletContext().getRealPath(JVD_DIR));
        Path filePath = imagesPath.resolve("./" + name);

        if (!Files.exists(imagesPath)) {
            Files.createDirectories(imagesPath);
        }

        // TODO: prevent overwriting existing?

        Files.writeString(filePath, contents);

        resp.sendRedirect(req.getContextPath() + "/main");
    }
}
