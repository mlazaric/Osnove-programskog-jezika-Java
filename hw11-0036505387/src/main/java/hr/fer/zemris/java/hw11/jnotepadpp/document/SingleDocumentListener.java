package hr.fer.zemris.java.hw11.jnotepadpp.document;

/**
 * Models a listener for the {@link SingleDocumentModel} object which is notified when the model's modified status
 * is changed or the file path is updated.
 *
 * @author Marko Lazarić
 */
public interface SingleDocumentListener {

    /**
     * The method is called when a {@link SingleDocumentModel}'s modified status has changed.
     *
     * @param model the model whose modified status has changed
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * The method is called when a {@link SingleDocumentModel}'s file path is updated.
     *
     * @param model the model whose file path has been updated
     */
    void documentFilePathUpdated(SingleDocumentModel model);

}
