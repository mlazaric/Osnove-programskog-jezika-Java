package hr.fer.zemris.java.hw07.observer2;

public class ChangeCounter implements IntegerStorageObserver {

    private int counter = 0;

    @Override
    public void valueChanged(IntegerStorageChange change) {
        ++counter;

        System.out.println("Number of value changes since tracking: " + counter);
    }
}
