package hr.fer.zemris.java.hw11.jnotepadpp.document;

public interface SingleDocumentListener {

    void documentModifyStatusUpdated(SingleDocumentModel model);
    void documentFilePathUpdated(SingleDocumentModel model);

}
