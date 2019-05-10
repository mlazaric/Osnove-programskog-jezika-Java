package hr.fer.zemris.java.hw07.observer2;

/**
 * Models a simple observer where the subject is an {@link IntegerStorage} and it fires
 * on every value change.
 *
 * @author Marko Lazarić
 */
public interface IntegerStorageObserver {

    /**
     * Called when the value of the {@link IntegerStorage} has been changed.
     *
     * @param change object containing a reference to the {@link IntegerStorage}, the old and the new value
     */
    void valueChanged(IntegerStorageChange change);

}