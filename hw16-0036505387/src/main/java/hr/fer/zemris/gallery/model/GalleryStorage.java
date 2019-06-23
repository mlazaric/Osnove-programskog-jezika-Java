package hr.fer.zemris.gallery.model;

import java.util.List;
import java.util.Set;

public interface GalleryStorage {

    Set<String> getTags();
    List<Image> getImagesForTag(String tag);

}
