package hr.fer.zemris.java.hw11.jnotepadpp.document;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

    private List<SingleDocumentModel> documents;
    private SingleDocumentModel currentDocument;

    private List<MultipleDocumentListener> listeners;
    private boolean shouldCopyOnWrite;

    public DefaultMultipleDocumentModel() {
        documents = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel prevDocument = currentDocument;

        currentDocument = new DefaultSingleDocumentModel(null, "");

        fireDocumentAdded(currentDocument);
        fireCurrentDocumentChanged(prevDocument, currentDocument);

        return currentDocument;
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        return currentDocument;
    }

    @Override
    public SingleDocumentModel loadDocument(Path path) {
        Objects.requireNonNull(path, "Path cannot be null.");

        Optional<SingleDocumentModel> model = documents.stream()
                                                       .filter(m -> path.equals(m.getFilePath()))
                                                       .findFirst();

        SingleDocumentModel prevDocument = currentDocument;

        if (model.isPresent()) {
            currentDocument = model.get();
        }
        else {
            String contents = null;

            try {
                contents = Files.readString(path);
            }
            catch (IOException exc) {
                throw new RuntimeException("Error encountered while reading file '" + path + "'.");
            }

            currentDocument = new DefaultSingleDocumentModel(path, contents);
            documents.add(currentDocument);

            fireDocumentAdded(currentDocument);
        }

        fireCurrentDocumentChanged(prevDocument, currentDocument);

        return currentDocument;
    }

    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        if (newPath == null) {
            newPath = model.getFilePath();
        }
        else {
            final Path path = newPath;

            Optional<SingleDocumentModel> model2 = documents.stream()
                                                            .filter(m -> path.equals(m.getFilePath()) &&
                                                                         !m.equals(model))
                                                            .findFirst();

            if (model2.isPresent()) {
                throw new RuntimeException("The specified file is already open: '" + newPath + "'.");
            }
        }

        Objects.requireNonNull(newPath, "newPath and model.getFilePath() cannot both be null.");

        try {
            Files.writeString(newPath, model.getTextComponent().getText());
        } catch (IOException e) {
            throw new RuntimeException("Error encountered while writing to '" + newPath + "'.");
        }

        model.setFilePath(newPath);
        model.setModified(false);
    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        documents.remove(model);
        fireDocumentRemoved(model);
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return documents.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return documents.get(index);
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documents.iterator();
    }

    private void copyOnWriteIfNecessary() {
        if (shouldCopyOnWrite) {
            listeners = new ArrayList<>(listeners);
        }
    }

    private void fireCurrentDocumentChanged(SingleDocumentModel prevDocument, SingleDocumentModel currentDocument) {
        copyOnWriteIfNecessary();

        listeners.forEach(l -> l.currentDocumentChanged(prevDocument, currentDocument));
    }

    private void fireDocumentAdded(SingleDocumentModel model) {
        copyOnWriteIfNecessary();

        listeners.forEach(l -> l.documentAdded(model));
    }

    private void fireDocumentRemoved(SingleDocumentModel model) {
        copyOnWriteIfNecessary();

        listeners.forEach(l -> l.documentRemoved(model));
    }
}
