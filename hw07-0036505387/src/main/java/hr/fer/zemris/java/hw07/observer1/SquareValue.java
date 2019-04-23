package hr.fer.zemris.java.hw07.observer1;

public class SquareValue implements IntegerStorageObserver {

    @Override
    public void valueChanged(IntegerStorage istorage) {
        int val = istorage.getValue();
        int val2 = val * val;

        System.out.println("Provided new value: " + val + ", square is " + val2);
    }

}
