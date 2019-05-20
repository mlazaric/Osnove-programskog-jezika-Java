package hr.fer.zemris.java.hw11.jnotepadpp.document;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.Objects;

public class DefaultSingleDocumentModel extends AbstractSingleDocumentModel {

    private Path path;
    private JTextArea textArea;
    private boolean modified;

    public DefaultSingleDocumentModel(Path path, String contents) {
        super();

        this.path = path;
        Objects.requireNonNull(contents, "Contents cannot be null.");
        this.textArea = new JTextArea(contents);
        this.modified = false;

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

}
