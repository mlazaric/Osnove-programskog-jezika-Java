package hr.fer.zemris.java.hw07.observer2;

/**
 * Integer storage observer that prints the squared new value after each value change.
 *
 * @author Marko Lazarić
 */
public class SquareValue implements IntegerStorageObserver {

    /**
     * Prints the squared new value after each value change.
     *
     * @param change object containing a reference to the {@link IntegerStorage}, the old and the new value
     */
    @Override
    public void valueChanged(IntegerStorageChange change) {
        int val = change.getNewValue();
        int val2 = val * val;

        System.out.println("Provided new value: " + val + ", square is " + val2);
    }

}
