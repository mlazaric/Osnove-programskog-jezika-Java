package hr.fer.zemris.java.hw07.observer1;

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
     * @param istorage the subject of this observer
     */
    @Override
    public void valueChanged(IntegerStorage istorage) {
        --times;

        int doubleVal = istorage.getValue() * 2;

        System.out.println("Double value: " + doubleVal);

        if (times == 0) {
            istorage.removeObserver(this);
        }
    }

}
