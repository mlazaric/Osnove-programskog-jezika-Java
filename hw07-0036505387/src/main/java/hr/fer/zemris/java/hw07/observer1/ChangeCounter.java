package hr.fer.zemris.java.hw07.observer1;

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
     * @param istorage the subject of this observer
     */
    @Override
    public void valueChanged(IntegerStorage istorage) {
        ++counter;

        System.out.println("Number of value changes since tracking: " + counter);
    }

}
