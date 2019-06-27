package hr.fer.zemris.gallery.model;

import java.util.List;
import java.util.Set;

/**
 * Models an abstract storage of a gallery which contains images which have tags.
 *
 * @author Marko Lazarić
 */
public interface GalleryStorage {

    /**
     * Returns all the used tags.
     *
     * @return all the used tags
     */
    Set<String> getTags();

    /**
     * Returns a list of images which are tagged with the specific tag.
     *
     * @param tag the tag to find the images for
     * @return a list of images which are tagged with the specific tag
     */
    List<Image> getImagesForTag(String tag);

    /**
     * Returns an image with the specified file name or null if there is no such image in this gallery.
     *
     * @param name the file name of the image to return
     * @return an image with the specified file name or null if there is no such image in this gallery
     */
    Image getImageFromName(String name);

}
