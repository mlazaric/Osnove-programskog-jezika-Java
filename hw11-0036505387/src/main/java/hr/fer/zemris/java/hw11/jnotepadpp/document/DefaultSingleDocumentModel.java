package hr.fer.zemris.java.hw11.jnotepadpp.document;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultSingleDocumentModel implements SingleDocumentModel {

    private Path path;
    private JTextArea textArea;
    private boolean modified;
    private List<SingleDocumentListener> listeners;
    private boolean shouldCopyOnWrite;

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

    private void copyOnWriteIfNecessary() {
        if (shouldCopyOnWrite) {
            listeners = new ArrayList<>(listeners);
        }
    }

    protected void fireDocumentModifyStatusUpdated() {
        shouldCopyOnWrite = true;

        listeners.forEach(l -> l.documentFilePathUpdated(this));

        shouldCopyOnWrite = false;
    }

    protected void fireDocumentFilePathUpdated() {
        shouldCopyOnWrite = true;

        listeners.forEach(l -> l.documentFilePathUpdated(this));

        shouldCopyOnWrite = false;
    }
}
