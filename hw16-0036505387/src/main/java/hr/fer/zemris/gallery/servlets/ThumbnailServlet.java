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

/**
 * A servlet which creates "thumbnail" versions of the images and serves them to the client. It uses local storage for
 * caching the created thumbnails.
 *
 * @author Marko Lazarić
 */
@WebServlet(urlPatterns = "/thumbnails/*")
public class ThumbnailServlet extends HttpServlet  {

    /**
     * The folder in which the full sized images are stored.
     */
    private final static String IMAGE_LOCATION = "/images";

    /**
     * The folder in which the thumbnails are cached.
     */
    private final static String THUMBNAIL_LOCATION = "/WEB-INF/thumbnails";

    /**
     * The width of the generated thumbnail.
     */
    private final static int THUMBNAIL_WIDTH = 150;

    /**
     * The height of the generated thumbnail.
     */
    private final static int THUMBNAIL_HEIGHT = 150;

    private static final long serialVersionUID = 5786755313526390948L;

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

    /**
     * Scales an existing image to the specified dimensions.
     *
     * @param image the image to scale
     * @param newWidth the width of the image after scaling
     * @param newHeight the height of the image after scaling
     * @return the existing image scaled to the specified dimensions
     */
    private static BufferedImage scaleImage(BufferedImage image, int newWidth, int newHeight) {
        Image scaled = image.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
        BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, image.getType());

        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.drawImage(scaled, 0, 0, null);
        graphics2D.dispose();

        return bufferedImage;
    }

}