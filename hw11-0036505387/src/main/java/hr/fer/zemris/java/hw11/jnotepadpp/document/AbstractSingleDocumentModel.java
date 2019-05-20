package hr.fer.zemris.java.hw11.jnotepadpp.document;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSingleDocumentModel implements SingleDocumentModel {

    private List<SingleDocumentListener> listeners;
    private boolean shouldCopyOnWrite;

    protected AbstractSingleDocumentModel() {
        listeners = new ArrayList<>();
        shouldCopyOnWrite = false;
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
