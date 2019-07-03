package hr.fer.zemris.gallery.model;

/**
 * Models a static provider of a {@link GalleryStorage} instance. Used for accessing it in the REST API and the servlets.
 *
 * @author Marko Lazarić
 */
public class GalleryStorageProvider {

    /**
     * The instance of the {@link GalleryStorage} to provide.
     */
    private static GalleryStorage instance;

    /**
     * Returns the currently stored instance of {@link GalleryStorage}.
     *
     * @return the currently stored instance of {@link GalleryStorage}
     */
    public static GalleryStorage getInstance() {
        return instance;
    }

    /**
     * Sets the {@link #instance} to the new value.
     *
     * @param instance the new value of {@link #instance}
     */
    public static void setInstance(GalleryStorage instance) {
        GalleryStorageProvider.instance = instance;
    }

}
