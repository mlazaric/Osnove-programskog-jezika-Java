package hr.fer.zemris.java.hw07.observer1;

/**
 * Integer storage observer that prints the squared new value after each value change.
 *
 * @author Marko Lazarić
 */
public class SquareValue implements IntegerStorageObserver {

    /**
     * Prints the squared new value after each value change.
     *
     * @param istorage the subject of this observer
     */
    @Override
    public void valueChanged(IntegerStorage istorage) {
        int val = istorage.getValue();
        int val2 = val * val;

        System.out.println("Provided new value: " + val + ", square is " + val2);
    }

}
