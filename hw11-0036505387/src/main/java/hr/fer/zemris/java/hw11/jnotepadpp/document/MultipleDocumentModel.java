package hr.fer.zemris.java.hw11.jnotepadpp.document;

import java.nio.file.Path;

/**
 * Models a collection of {@link SingleDocumentModel}.
 *
 * @author Marko Lazarić
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {

    /**
     * Creates a new blank {@link SingleDocumentModel}.
     *
     * @return the newly created {@link SingleDocumentModel}
     */
    SingleDocumentModel createNewDocument();

    /**
     * Returns the currently selected {@link SingleDocumentModel} or null if no {@link SingleDocumentModel} is selected.
     *
     * @return the currently selected {@link SingleDocumentModel} or null
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * Loads the document from the disk and adds it to the collection.
     *
     * @param path the path to the document on the disk
     * @return the newly created {@link SingleDocumentModel}
     */
    SingleDocumentModel loadDocument(Path path);

    /**
     * Saves the {@link SingleDocumentModel} to disk using the specified path. If the second argument is null, the file
     * path stored in the {@link SingleDocumentModel} is used.
     *
     * @param model the model to save to disk
     * @param newPath the optional new path to use
     */
    void saveDocument(SingleDocumentModel model, Path newPath);

    /**
     * Closes the specified {@link SingleDocumentModel}.
     *
     * @param model the model to close
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * Adds a {@link MultipleDocumentListener} to listeners.
     *
     * @param l the listener to add
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Removes the {@link MultipleDocumentListener} from listeners.
     *
     * @param l the listener to remove
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Returns the number of {@link SingleDocumentModel} that are currently open.
     *
     * @return the number of {@link SingleDocumentModel} that are currently open
     */
    int getNumberOfDocuments();

    /**
     * Returns the {@link SingleDocumentModel} at the specified index.
     *
     * @param index the index of the {@link SingleDocumentModel}
     * @return the {@link SingleDocumentModel} at the specified index
     */
    SingleDocumentModel getDocument(int index);
}
