package hr.fer.zemris.gallery.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileGalleryStorage implements GalleryStorage {

    private final Map<String, List<Image>> images = new HashMap<>();

    public FileGalleryStorage(Path pathToFile) {
        if (!Files.isReadable(pathToFile)) {
            throw new IllegalArgumentException("File " + pathToFile.toString() + " is not readable.");
        }

        loadFromFile(pathToFile);
    }

    public FileGalleryStorage(String pathToFile) {
        this(Paths.get(pathToFile));
    }

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

            for (String tag : tags) {
                List<Image> imagesForTag = images.get(tag);

                if (imagesForTag == null) {
                    imagesForTag = new ArrayList<>();

                    images.put(tag, imagesForTag);
                }

                imagesForTag.add(image);
            }
        }
    }

    @Override
    public Set<String> getTags() {
        return images.keySet();
    }

    @Override
    public List<Image> getImagesForTag(String tag) {
        return images.get(tag);
    }

}
