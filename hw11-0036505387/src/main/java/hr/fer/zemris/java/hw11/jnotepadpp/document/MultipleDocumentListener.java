package hr.fer.zemris.java.hw11.jnotepadpp.document;

/**
 * Models a listener for the {@link MultipleDocumentModel} object which is notified when the currently selected document
 * is changed, when a new document is added and when a document is removed.
 *
 * @author Marko Lazarić
 */
public interface MultipleDocumentListener {

    /**
     * The method is called when the currently selected {@link SingleDocumentModel} is changed.
     * One of the arguments can be null, but not both.
     *
     * @param previousModel the previously selected model, can be null
     * @param currentModel the currently selected model, can be null
     */
    void currentDocumentChanged(SingleDocumentModel previousModel,
                                SingleDocumentModel currentModel);

    /**
     * The method is called when a document is added to the {@link MultipleDocumentModel}.
     *
     * @param model the model that was added
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * The method is called when a document is removed from the {@link MultipleDocumentModel}.
     *
     * @param model the model that was removed
     */
    void documentRemoved(SingleDocumentModel model);

}