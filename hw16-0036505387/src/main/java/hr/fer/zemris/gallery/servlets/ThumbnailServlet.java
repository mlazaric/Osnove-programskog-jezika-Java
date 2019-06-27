package hr.fer.zemris.gallery.servlets;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(urlPatterns = "/thumbnails/*")
public class ThumbnailServlet extends HttpServlet  {

    private final static String IMAGE_LOCATION = "/images";
    private final static String THUMBNAIL_LOCATION = "/WEB-INF/thumbnails";

    private final static int THUMBNAIL_WIDTH = 150;
    private final static int THUMBNAIL_HEIGHT = 150;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String imageName = req.getPathInfo().substring(1);

        Path thumbnailFolder = Paths.get(req.getServletContext().getRealPath(THUMBNAIL_LOCATION));
        Path imageFolder = Paths.get(req.getServletContext().getRealPath(IMAGE_LOCATION));

        // If the thumbnails folder does not exist, create it
        if (Files.notExists(thumbnailFolder)) {
            Files.createDirectories(thumbnailFolder);
        }

        Path thumbnail = thumbnailFolder.resolve("./" + imageName);
        Path image = imageFolder.resolve("./" + imageName);

        // If the user is trying to access a thumbnail of an image that does not exist, return 404
        if (Files.notExists(image)) {
            resp.sendError(404);
            return;
        }

        String fileType = imageName.substring(imageName.lastIndexOf('.') + 1).toLowerCase();

        // For simplicity's sake, we can assume we only serve PNG and JPG images, requesting anything else should return
        // a 404
        if (!(fileType.equals("png") || fileType.equals("jpg"))) {
            resp.sendError(404);
            return;
        }

        resp.setContentType("image/" + fileType);

        BufferedImage bufferedThumbnail;

        // Load already generated thumbnail image if it exists, otherwise create a thumbnail by downscaling the original
        // image, save the resulting thumbnail and serve it to the user
        if (Files.notExists(thumbnail)) {
            BufferedImage bufferedImage = ImageIO.read(image.toFile());

            bufferedThumbnail = scaleImage(bufferedImage, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

            ImageIO.write(bufferedThumbnail, fileType, thumbnail.toFile());
        }
        else {
            bufferedThumbnail = ImageIO.read(thumbnail.toFile());
        }

        ImageIO.write(bufferedThumbnail, fileType, resp.getOutputStream());
    }

    private static BufferedImage scaleImage(BufferedImage image, int newWidth, int newHeight) {
        Image scaled = image.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
        BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, image.getType());

        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.drawImage(scaled, 0, 0, null);
        graphics2D.dispose();

        return bufferedImage;
    }

}