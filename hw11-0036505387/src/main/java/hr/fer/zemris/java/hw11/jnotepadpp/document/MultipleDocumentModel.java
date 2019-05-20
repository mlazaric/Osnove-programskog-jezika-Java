package hr.fer.zemris.java.hw11.jnotepadpp.document;

import java.nio.file.Path;

public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
    SingleDocumentModel createNewDocument();
    SingleDocumentModel getCurrentDocument();
    SingleDocumentModel loadDocument(Path path);
    void saveDocument(SingleDocumentModel model, Path newPath);
    void closeDocument(SingleDocumentModel model);
    void addMultipleDocumentListener(MultipleDocumentListener l);
    void removeMultipleDocumentListener(MultipleDocumentListener l);
    int getNumberOfDocuments();
    SingleDocumentModel getDocument(int index);
}
