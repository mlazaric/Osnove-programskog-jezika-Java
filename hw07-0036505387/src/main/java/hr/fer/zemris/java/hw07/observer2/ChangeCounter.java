package hr.fer.zemris.java.hw07.observer2;

/**
 * Integer storage observer that counts the number of times the value of
 * {@link IntegerStorage} has been changed.
 *
 * @author Marko Lazarić
 */
public class ChangeCounter implements IntegerStorageObserver {

    /**
     * The number of times the value of {@link IntegerStorage} has been changed.
     */
    private int counter = 0;

    /**
     * Increments the counter and prints it.
     *
     * @param change object containing a reference to the {@link IntegerStorage}, the old and the new value
     */
    @Override
    public void valueChanged(IntegerStorageChange change) {
        ++counter;

        System.out.println("Number of value changes since tracking: " + counter);
    }
}
