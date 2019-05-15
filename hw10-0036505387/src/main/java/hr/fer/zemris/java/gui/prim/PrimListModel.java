package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

/**
 * A simple ListModel which generates prime numbers.
 *
 * @author Marko Lazarić
 *
 * @see #next()
 */
public class PrimListModel implements ListModel<Integer> {

    /**
     * The listeners that should be notified after a new prime has been generated.
     */
    private final List<ListDataListener> listeners;

    /**
     * The currently generated prime numbers.
     */
    private final List<Integer> primeNumbers;

    /**
     * The current number.
     *
     * @see #addNextPrime()
     */
    private int currentNumber = 0;

    /**
     * Creates a new {@link PrimListModel} with 1 in it.
     */
    public PrimListModel() {
        this.primeNumbers = new ArrayList<>();
        this.listeners = new ArrayList<>();

        addNextPrime();
    }

    /**
     * Generates the next prime and adds it to the collection.
     */
    private void addNextPrime() {
        ++currentNumber;

        while (!isPrime(currentNumber)) {
            ++currentNumber;
        }

        primeNumbers.add(currentNumber);
    }

    /**
     * Checks whether the argument is a prime number.
     *
     * @param currentNumber the number to check
     * @return true if it is a prime number,
     *         false otherwise
     */
    private boolean isPrime(int currentNumber) {
        int upperLimit = (int) ceil(sqrt(currentNumber));

        if ((currentNumber % 2) == 0 && currentNumber != 2) {
            return false;
        }

        for (int factor = 3; factor <= upperLimit; factor += 2) {
            if ((currentNumber % factor) == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Notifies all registered listeners of a newly generated prime.
     */
    private void notifyListeners() {
        for (ListDataListener listener : listeners) {
            listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED,
                    primeNumbers.size() - 1, primeNumbers.size() - 1));
        }
    }

    /**
     * Generates a new prime and notifies all the registered listeners.
     */
    public void next() {
        addNextPrime();
        notifyListeners();
    }

    @Override
    public int getSize() {
        return primeNumbers.size();
    }

    @Override
    public Integer getElementAt(int index) {
        return primeNumbers.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
}
