package hr.fer.zemris.java.hw07.observer2;

public class SquareValue implements IntegerStorageObserver {

    @Override
    public void valueChanged(IntegerStorageChange change) {
        int val = change.getNewValue();
        int val2 = val * val;

        System.out.println("Provided new value: " + val + ", square is " + val2);
    }

}
