package hr.fer.zemris.gallery.listeners;

import hr.fer.zemris.gallery.Gallery;
import hr.fer.zemris.gallery.GalleryProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class GalleryListener implements ServletContextListener {

    private static final String GALLERY_FILE_PATH = "/WEB-INF/opisnik.txt";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        GalleryProvider.setInstance(new Gallery(sce.getServletContext().getRealPath(GALLERY_FILE_PATH)));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        GalleryProvider.setInstance(null);
    }

}
