package hr.fer.zemris.java.hw07.observer2;

import java.util.Objects;

/**
 * Represents a value change for {@link IntegerStorage}.
 *
 * @author Marko Lazarić
 *
 * @see IntegerStorageObserver
 */
public class IntegerStorageChange {

    /**
     * A reference to the {@link IntegerStorage} whose value was changed.
     */
    private final IntegerStorage integerStorage;

    /**
     * The old value.
     */
    private final int oldValue;

    /**
     * The new value.
     */
    private final int newValue;

    /**
     * Creates a new {@link IntegerStorageChange} with the given arguments.
     *
     * @param integerStorage a reference to the {@link IntegerStorage} whose value was changed
     * @param oldValue the old value
     * @param newValue the new value
     *
     * @throws NullPointerException if {@code integerStorage} is {@code null}
     */
    public IntegerStorageChange(IntegerStorage integerStorage, int oldValue, int newValue) {
        this.integerStorage = Objects.requireNonNull(integerStorage, "Cannot construct a change for a null integer storage.");
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Returns the new value.
     *
     * @return the new value
     */
    public int getNewValue() {
        return newValue;
    }

    /**
     * Returns the old value.
     *
     * @return the old value
     */
    public int getOldValue() {
        return oldValue;
    }

    /**
     * Returns a reference to the {@link IntegerStorage} whose value was changed.
     *
     * @return a reference to the {@link IntegerStorage} whose value was changed
     */
    public IntegerStorage getIntegerStorage() {
        return integerStorage;
    }
}
