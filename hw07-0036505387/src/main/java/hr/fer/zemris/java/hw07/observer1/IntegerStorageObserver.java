package hr.fer.zemris.java.hw07.observer1;

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
     * @param istorage the subject of this observer
     */
    void valueChanged(IntegerStorage istorage);

}