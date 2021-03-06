package hr.fer.zemris.gallery.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * A concrete implementation of {@link GalleryStorage} using a file to store the information about the images within
 * the gallery.
 *
 * @author Marko Lazarić
 */
public class FileGalleryStorage implements GalleryStorage {

    /**
     * The mapping of tag names to images tagged with that tag.
     */
    private final Map<String, List<Image>> tagsToImages = new HashMap<>();

    /**
     * The mapping of image names to their image object.
     */
    private final Map<String, Image> namesToImages = new HashMap<>();

    /**
     * Creates a new {@link FileGalleryStorage} with the given argument.
     *
     * @param pathToFile path to the file containing the information about the images
     *
     * @throws IllegalArgumentException if the file is not readable or parsable
     */
    public FileGalleryStorage(Path pathToFile) {
        if (!Files.isReadable(pathToFile)) {
            throw new IllegalArgumentException("File " + pathToFile.toString() + " is not readable.");
        }

        loadFromFile(pathToFile);
    }

    /**
     * Creates a new {@link FileGalleryStorage} with the given argument.
     *
     * @param pathToFile path to the file containing the information about the images
     *
     * @throws IllegalArgumentException if the file is not readable or parsable
     */
    public FileGalleryStorage(String pathToFile) {
        this(Paths.get(pathToFile));
    }

    /**
     * Loads the information about the images from the file.
     *
     * @param pathToFile path to the file containing the information about the images
     *
     * @throws IllegalArgumentException if the file is not parsable
     */
    private void loadFromFile(Path pathToFile) {
        List<String> lines;

        try {
            lines = Files.readAllLines(pathToFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if ((lines.size() % 3) != 0) {
            throw new IllegalArgumentException("Invalid file, line count should be a multiple of 3.");
        }

        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex += 3) {
            String imageName = lines.get(lineIndex).strip();
            String description = lines.get(lineIndex + 1).strip();
            String[] tags = lines.get(lineIndex + 2).strip().split(", ");

            Image image = new Image(imageName, description, tags);

            namesToImages.put(image.getFileName(), image);

            for (String tag : tags) {
                List<Image> imagesForTag = tagsToImages.get(tag);

                if (imagesForTag == null) {
                    imagesForTag = new ArrayList<>();

                    tagsToImages.put(tag, imagesForTag);
                }

                imagesForTag.add(image);
            }
        }
    }

    @Override
    public Set<String> getTags() {
        return tagsToImages.keySet();
    }

    @Override
    public List<Image> getImagesForTag(String tag) {
        return tagsToImages.get(tag);
    }

    @Override
    public Image getImageFromName(String name) {
        return namesToImages.get(name);
    }

}
