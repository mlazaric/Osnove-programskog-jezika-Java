package hr.fer.zemris.java.hw07.observer2;

public class DoubleValue implements IntegerStorageObserver {

    private int times;

    public DoubleValue(int times) {
        if (times < 1) {
            throw new IllegalArgumentException("Times must be at least 1.");
        }

        this.times = times;
    }

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
