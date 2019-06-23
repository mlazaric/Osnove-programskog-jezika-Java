package hr.fer.zemris.gallery.model;

public class GalleryProvider {

    private static GalleryStorage instance;

    public static GalleryStorage getInstance() {
        return instance;
    }

    public static void setInstance(GalleryStorage instance) {
        GalleryProvider.instance = instance;
    }
}
