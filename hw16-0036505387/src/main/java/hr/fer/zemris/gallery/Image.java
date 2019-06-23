package hr.fer.zemris.gallery;

import java.util.Arrays;
import java.util.Objects;

public class Image {

    private final String fileName;
    private final String description;
    private final String[] tags;

    public Image(String fileName, String description, String[] tags) {
        this.fileName = Objects.requireNonNull(fileName, "File name cannot be null.");
        this.description = Objects.requireNonNull(description, "Description cannot be null.");
        this.tags = Objects.requireNonNull(tags, "Tags cannot be null.");
    }

    public String getFileName() {
        return fileName;
    }

    public String getDescription() {
        return description;
    }

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
