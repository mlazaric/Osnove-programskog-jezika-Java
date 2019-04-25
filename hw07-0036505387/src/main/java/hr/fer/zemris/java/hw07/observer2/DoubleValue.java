package hr.fer.zemris.java.hw07.observer2;

/**
 * Integer storage observer that prints the doubled value for the first {@code times} the value changes.
 *
 * @author Marko Lazarić
 */
public class DoubleValue implements IntegerStorageObserver {

    /**
     * The number of value changes it should observe before removing itself.
     */
    private int times;

    /**
     * Creates a new {@link DoubleValue} observer.
     *
     * @param times the number of value changes it should observe before removing itself
     *
     * @throws IllegalArgumentException if {@code times} is less than 1
     */
    public DoubleValue(int times) {
        if (times < 1) {
            throw new IllegalArgumentException("Times must be at least 1.");
        }

        this.times = times;
    }

    /**
     * Prints the doubled value for the first {@code times} the value changes. Afterwards it removes
     * itself from the observers.
     *
     * @param change object containing a reference to the {@link IntegerStorage}, the old and the new value
     */
    @Override
    public void valueChanged(IntegerStorageChange change) {
        --times;

        int doubleVal = change.getNewValue() * 2;

        System.out.println("Double value: " + doubleVal);

        if (times == 0) {
            change.getIntegerStorage().removeObserver(this);
        }
    }

}
