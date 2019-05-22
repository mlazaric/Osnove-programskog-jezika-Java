package hr.fer.zemris.java.hw11.jnotepadpp.document;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.*;

/**
 * Implements {@link MultipleDocumentModel} using a {@link JTabbedPane} and {@link DefaultSingleDocumentModel}.
 *
 * @author Marko Lazarić
 */
@SuppressWarnings("serial")
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel, MultipleDocumentListener {

    /**
     * The list of {@link SingleDocumentModel}s that are currently open.
     */
    private List<SingleDocumentModel> documents;

    /**
     * The currently selected {@link SingleDocumentModel}. Can be null if no document is selected.
     */
    private SingleDocumentModel currentDocument;

    /**
     * The listeners that should be notified of any changes.
     */
    private List<MultipleDocumentListener> listeners;

    /**
     * Whether {@link #listeners} should be copied before writing to prevent {@link java.util.ConcurrentModificationException}.
     */
    private boolean shouldCopyOnWrite;

    /**
     * The icon used to indicate isModified = false.
     */
    private final ImageIcon savedIcon = loadIcon("icons/saved.png");

    /**
     * The icon used to indicate isModified = true.
     */
    private final ImageIcon modifiedIcon = loadIcon("icons/modified.png");

    /**
     * Creates a new {@link DefaultMultipleDocumentModel}.
     */
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

    /**
     * Copies {@link #listeners} if it currently being iterated over, to prevent {@link java.util.ConcurrentModificationException}.
     */
    private void copyOnWriteIfNecessary() {
        if (shouldCopyOnWrite) {
            listeners = new ArrayList<>(listeners);
        }
    }

    /**
     * Notifies all subscribed listeners of a change to the current document.
     */
    private void fireCurrentDocumentChanged(SingleDocumentModel prevDocument, SingleDocumentModel currentDocument) {
        shouldCopyOnWrite = true;

        listeners.forEach(l -> l.currentDocumentChanged(prevDocument, currentDocument));

        shouldCopyOnWrite = false;
    }

    /**
     * Notifies all subscribed listeners of an added document.
     */
    private void fireDocumentAdded(SingleDocumentModel model) {
        shouldCopyOnWrite = true;

        listeners.forEach(l -> l.documentAdded(model));

        shouldCopyOnWrite = false;
    }

    /**
     * Notifies all subscribed listeners of a removed document.
     */
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
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(model.getTextComponent()), BorderLayout.CENTER);

        addTab("", panel);
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

    /**
     * Updates the title, tooltip and icon of the tab.
     *
     * @param model the model whose tab is updated
     */
    private void updateTab(SingleDocumentModel model) {
        String title = "(unnamed)";
        String toolTip = "(unnamed)";
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

    /**
     * Loads icon from disk.
     *
     * @param path the path to the icon
     * @return the loaded {@link ImageIcon}
     *
     * @throws RuntimeException if an exception occurs while loading the icon
     */
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
