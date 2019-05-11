package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public class PrimListModel implements ListModel<Integer> {

    private final List<ListDataListener> listeners;
    private final List<Integer> primeNumbers;
    private int currentNumber;

    public PrimListModel() {
        this.primeNumbers = new ArrayList<>();
        this.listeners = new ArrayList<>();

        addNextPrime();
    }

    private void addNextPrime() {
        ++currentNumber;

        while (!isPrime(currentNumber)) {
            ++currentNumber;
        }

        primeNumbers.add(currentNumber);
    }

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

    private void notifyObservers() {
        for (ListDataListener listener : listeners) {
            listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED,
                    primeNumbers.size() - 1, primeNumbers.size() - 1));
        }
    }

    public void next() {
        addNextPrime();
        notifyObservers();
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
