package hr.fer.zemris.java.hw07.observer2;

import java.util.Objects;

public class IntegerStorageChange {

    private final IntegerStorage integerStorage;
    private final int oldValue;
    private final int newValue;


    public IntegerStorageChange(IntegerStorage integerStorage, int oldValue, int newValue) {
        this.integerStorage = Objects.requireNonNull(integerStorage, "Cannot construct a change for a null integer storage.");
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public int getNewValue() {
        return newValue;
    }

    public int getOldValue() {
        return oldValue;
    }

    public IntegerStorage getIntegerStorage() {
        return integerStorage;
    }
}
