package hr.fer.zemris.java.hw07.observer2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IntegerStorage {

    private int value;
    private List<IntegerStorageObserver> observers; // use ArrayList here!!!

    public IntegerStorage(int initialValue) {
        this.value = initialValue;
    }

    public void addObserver(IntegerStorageObserver observer) {
        Objects.requireNonNull(observer, "Cannot add null observer.");

        if (observers == null) {
            observers = new ArrayList<>();
        }
        else {
            // Copy the List before writing.
            observers = new ArrayList<>(observers);
        }

        if (observers.contains(observer)) {
            return;
        }

        observers.add(observer);
    }

    public void removeObserver(IntegerStorageObserver observer) {
        Objects.requireNonNull(observer, "Cannot remove null observer.");

        if (observers == null || !observers.contains(observer)) {
            throw new IllegalArgumentException("The observer was not registered, cannot remove it.");
        }
        else {
            // Copy the List before writing.
            observers = new ArrayList<>(observers);
        }

        observers.remove(observer);
    }


    public void clearObservers() {
        if (observers == null) {
            return;
        }

        observers.clear();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        // Only if new value is different than the current value:
        if(this.value != value) {

            IntegerStorageChange change = new IntegerStorageChange(this, this.value, value);
            // Update current value
            this.value = value;

            // Notify all registered observers
            if (observers != null) {
                for (IntegerStorageObserver observer : observers) {
                    observer.valueChanged(change);
                }
            }
        }
    }
}