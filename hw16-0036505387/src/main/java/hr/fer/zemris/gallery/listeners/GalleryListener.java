package hr.fer.zemris.gallery.listeners;

import hr.fer.zemris.gallery.model.FileGalleryStorage;
import hr.fer.zemris.gallery.model.GalleryStorageProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * A listener which loads the {@link hr.fer.zemris.gallery.model.GalleryStorage} object into {@link GalleryStorageProvider} on
 * its initialisation and removes it when it is destroyed.
 *
 * @author Marko Lazarić
 */
@WebListener
public class GalleryListener implements ServletContextListener {

    /**
     * The path to the file used for {@link FileGalleryStorage}.
     */
    private static final String GALLERY_FILE_PATH = "/WEB-INF/opisnik.txt";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        GalleryStorageProvider.setInstance(new FileGalleryStorage(sce.getServletContext().getRealPath(GALLERY_FILE_PATH)));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        GalleryStorageProvider.setInstance(null);
    }

}
