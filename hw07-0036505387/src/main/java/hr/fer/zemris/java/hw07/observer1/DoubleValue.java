package hr.fer.zemris.java.hw07.observer1;

public class DoubleValue implements IntegerStorageObserver {

    private int times;

    public DoubleValue(int times) {
        if (times < 1) {
            throw new IllegalArgumentException("Times must be at least 1.");
        }

        this.times = times;
    }

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
