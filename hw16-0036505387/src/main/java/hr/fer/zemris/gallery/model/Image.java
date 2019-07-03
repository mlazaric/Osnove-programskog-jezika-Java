package hr.fer.zemris.gallery.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Models an image within the gallery app. An image is represented by its file name, description and an array of tags.
 *
 * @author Marko Lazarić
 */
public class Image {

    /**
     * The file name of the image.
     */
    private final String fileName;

    /**
     * The description of the image.
     */
    private final String description;

    /**
     * The tags describing this image.
     */
    private final String[] tags;

    /**
     * Creates a new {@link Image} with the given arguments.
     *
     * @param fileName the file name of the image
     * @param description the description of the image
     * @param tags the tags describing this image
     *
     * @throws NullPointerException if any of the arguments are null
     */
    public Image(String fileName, String description, String[] tags) {
        this.fileName = Objects.requireNonNull(fileName, "File name cannot be null.");
        this.description = Objects.requireNonNull(description, "Description cannot be null.");
        this.tags = Objects.requireNonNull(tags, "Tags cannot be null.");
    }

    /**
     * Returns the file name of the image.
     *
     * @return the file name of the image
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the description of the image.
     *
     * @return the description of the image
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the tags describing this image.
     *
     * @return the tags describing this image
     */
    public String[] getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "Image{" +
                "fileName='" + fileName + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(fileName, image.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName);
    }

}
