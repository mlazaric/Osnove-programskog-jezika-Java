package hr.fer.zemris.java.hw11.jnotepadpp.document;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Models a single document represented by a {@link javax.swing.text.JTextComponent} and an optional {@link Path}.
 *
 * @author Marko Lazarić
 */
public interface SingleDocumentModel {

    /**
     * Returns the {@link javax.swing.text.JTextComponent} representing this document.
     *
     * @return the {@link javax.swing.text.JTextComponent} representing this document
     */
    JTextArea getTextComponent();

    /**
     * Returns the path to the document or null if the path is not set.
     *
     * @return the path to the document or null if the path is not set
     */
    Path getFilePath();

    /**
     * Sets the file path to the argument and notifies listeners.
     *
     * @param path the new file path
     */
    void setFilePath(Path path);

    /**
     * Returns whether the document has been modified since opening it.
     *
     * @return true if it has been modified, false otherwise
     */
    boolean isModified();

    /**
     * Sets the modified status of the document to the argument and notifies listeners.
     *
     * @param modified the new modified status of the document
     */
    void setModified(boolean modified);

    /**
     * Adds a {@link SingleDocumentListener} to listeners.
     *
     * @param l the listener to add
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * Removes the {@link SingleDocumentListener} from listeners.
     *
     * @param l the listener to remove
     */
    void removeSingleDocumentListener(SingleDocumentListener l);

}