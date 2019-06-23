package hr.fer.zemris.gallery;

public class GalleryProvider {

    private static Gallery instance;

    public static Gallery getInstance() {
        return instance;
    }

    public static void setInstance(Gallery instance) {
        GalleryProvider.instance = instance;
    }
}
