package hr.fer.zemris.java.hw11.jnotepadpp.document;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel, MultipleDocumentListener {

    private List<SingleDocumentModel> documents;
    private SingleDocumentModel currentDocument;

    private List<MultipleDocumentListener> listeners;
    private boolean shouldCopyOnWrite;

    private final ImageIcon savedIcon = loadIcon("icons/saved.png");
    private final ImageIcon modifiedIcon = loadIcon("icons/modified.png");

    public DefaultMultipleDocumentModel() {
        documents = new ArrayList<>();
        listeners = new ArrayList<>();

        listeners.add(this);

        addChangeListener(l -> {
            SingleDocumentModel prevDocument = currentDocument;
            int index = getSelectedIndex();

            if (index == -1) {
                currentDocument = null;
            }
            else {
                currentDocument = documents.get(index);
            }

            fireCurrentDocumentChanged(prevDocument, currentDocument);
        });
    }

    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel prevDocument = currentDocument;

        currentDocument = new DefaultSingleDocumentModel(null, "");

        documents.add(currentDocument);
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
        fireDocumentRemoved(model);
        documents.remove(model);

        if (documents.isEmpty()) {
            currentDocument = null;
        }
        else {
            currentDocument = documents.get(0);
        }

        fireCurrentDocumentChanged(model, currentDocument);
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        copyOnWriteIfNecessary();

        listeners.add(l);
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        copyOnWriteIfNecessary();

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
        shouldCopyOnWrite = true;

        listeners.forEach(l -> l.currentDocumentChanged(prevDocument, currentDocument));

        shouldCopyOnWrite = false;
    }

    private void fireDocumentAdded(SingleDocumentModel model) {
        shouldCopyOnWrite = true;

        listeners.forEach(l -> l.documentAdded(model));

        shouldCopyOnWrite = false;
    }

    private void fireDocumentRemoved(SingleDocumentModel model) {
        shouldCopyOnWrite = true;

        listeners.forEach(l -> l.documentRemoved(model));

        shouldCopyOnWrite = false;
    }

    @Override
    public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        if (currentModel == null) {
            return;
        }

        setSelectedIndex(documents.indexOf(currentModel));
    }

    @Override
    public void documentAdded(SingleDocumentModel model) {
        addTab("", new JScrollPane(model.getTextComponent()));
        updateTab(model);

        model.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                updateTab(model);
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                updateTab(model);
            }
        });
    }

    private void updateTab(SingleDocumentModel model) {
        String title = "(unnamed)";
        String toolTip = "";
        Path path = model.getFilePath();

        if (path != null) {
            title = path.getFileName().toString();
            toolTip = path.toAbsolutePath().toString();
        }

        int index = documents.indexOf(model);

        if (index != -1) {
            setTitleAt(index, title);
            setToolTipTextAt(index, toolTip);
            setIconAt(index, model.isModified() ? modifiedIcon : savedIcon);
        }
    }

    @Override
    public void documentRemoved(SingleDocumentModel model) {
        remove(documents.indexOf(model));
    }

    private ImageIcon loadIcon(String path) {
        InputStream is = this.getClass().getResourceAsStream(path);

        if (is == null) {
            throw new IllegalArgumentException("Could not load icon: " + path);
        }

        byte[] bytes = null;

        try {
            bytes = is.readAllBytes();

            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ImageIcon(bytes);
    }
}
