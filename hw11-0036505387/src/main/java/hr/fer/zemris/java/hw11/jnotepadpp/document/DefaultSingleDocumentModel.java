package hr.fer.zemris.java.hw11.jnotepadpp.document;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implements {@link SingleDocumentModel} using a {@link JTextArea}.
 *
 * @author Marko Lazarić
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {

    /**
     * The path to the document on the disk, can be null.
     */
    private Path path;

    /**
     * The text component used to show and edit the contents of the {@link SingleDocumentModel}.
     */
    private JTextArea textArea;

    /**
     * Whether it has been modified since the last loading or saving.
     */
    private boolean modified;

    /**
     * The listeners that should be notified of any changes.
     */
    private List<SingleDocumentListener> listeners;

    /**
     * Whether {@link #listeners} should be copied before writing to prevent {@link java.util.ConcurrentModificationException}.
     */
    private boolean shouldCopyOnWrite;

    /**
     * Creates a new {@link DefaultMultipleDocumentModel} with the given arguments.
     *
     * @param path the optional path to the document on the disk, can be null
     * @param contents the contents of the document, cannot be null
     *
     * @throws NullPointerException if the second argument is null
     */
    public DefaultSingleDocumentModel(Path path, String contents) {
        this.path = path;
        Objects.requireNonNull(contents, "Contents cannot be null.");
        this.textArea = new JTextArea(contents);
        this.modified = false;

        listeners = new ArrayList<>();

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setModified(true);
            }
        });
    }

    @Override
    public JTextArea getTextComponent() {
        return textArea;
    }

    @Override
    public Path getFilePath() {
        return path;
    }

    @Override
    public void setFilePath(Path path) {
        this.path = path;

        fireDocumentFilePathUpdated();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modified) {
        boolean prevModified = this.modified;

        this.modified = modified;

        if (modified != prevModified) {
            fireDocumentModifyStatusUpdated();
        }
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        copyOnWriteIfNecessary();

        listeners.add(l);
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        copyOnWriteIfNecessary();

        listeners.remove(l);
    }

    /**
     * Copies {@link #listeners} if it currently being iterated over, to prevent {@link java.util.ConcurrentModificationException}.
     */
    private void copyOnWriteIfNecessary() {
        if (shouldCopyOnWrite) {
            listeners = new ArrayList<>(listeners);
        }
    }

    /**
     * Notifies all subscribed listeners of a modified status update.
     */
    private void fireDocumentModifyStatusUpdated() {
        shouldCopyOnWrite = true;

        listeners.forEach(l -> l.documentModifyStatusUpdated(this));

        shouldCopyOnWrite = false;
    }

    /**
     * Notifies all subscribed listeners of a file path update.
     */
    private void fireDocumentFilePathUpdated() {
        shouldCopyOnWrite = true;

        listeners.forEach(l -> l.documentFilePathUpdated(this));

        shouldCopyOnWrite = false;
    }
}
