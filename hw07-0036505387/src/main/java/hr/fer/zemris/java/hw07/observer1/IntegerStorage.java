package hr.fer.zemris.java.hw07.observer1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stores an integer value and fires an event every time the value is changed.
 *
 * @author Marko Lazarić
 */
public class IntegerStorage {

    /**
     * The value stored in this {@link IntegerStorage}.
     */
    private int value;

    /**
     * List of observers that should be notified on value change.
     */
    private List<IntegerStorageObserver> observers;

    /**
     * Creates an {@link IntegerStorage} with the given initial value.
     *
     * @param initialValue the value stored in this {@link IntegerStorage}
     */
    public IntegerStorage(int initialValue) {
        this.value = initialValue;
    }

    /**
     * Adds the observer to the list of observers if it is not already in there.
     *
     * @param observer the observer to add
     *
     * @throws NullPointerException if {@code observer} is {@code null}
     */
    public void addObserver(IntegerStorageObserver observer) {
        Objects.requireNonNull(observer, "Cannot add null observer.");

        if (observers == null) {
            observers = new ArrayList<>();
        }
        else if (observers.contains(observer)) {
            return;
        }

        // Copy the List before writing.
        observers = new ArrayList<>(observers);

        observers.add(observer);
    }

    /**
     * Removes the observer from the list of observers if it is in there.
     * Otherwise it does nothing.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(IntegerStorageObserver observer) {
        if (observers == null || !observers.contains(observer)) {
            return;
        }
        else {
            // Copy the List before writing.
            observers = new ArrayList<>(observers);
        }

        observers.remove(observer);
    }


    /**
     * Clears the list of observers.
     */
    public void clearObservers() {
        if (observers == null) {
            return;
        }

        // Does not modify the List (using .clear()) so it does not cause
        // an exception if it is called from an observer.
        observers = null;
    }

    /**
     * Returns the current value of the {@link IntegerStorage}.
     *
     * @return the current value of the {@link IntegerStorage}
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the new value of the {@link IntegerStorage} and notifies all the observers.
     *
     * @param value the new value of the {@link IntegerStorage}
     */
    public void setValue(int value) {
        // Only if new value is different than the current value:
        if (this.value != value) {
            // Update current value
            this.value = value;

            // Notify all registered observers
            if (observers != null) {
                for (IntegerStorageObserver observer : observers) {
                    observer.valueChanged(this);
                }
            }
        }
    }
}