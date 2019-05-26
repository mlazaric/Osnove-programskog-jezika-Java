package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Models a map where each entry is a stack.
 *
 * @author Marko Lazarić
 */
public class ObjectMultistack {

    /**
     * The mapping of keys to stack tops.
     */
    private Map<String, MultistackEntry> map;

    /**
     * Creates a new {@link ObjectMultistack}.
     */
    public ObjectMultistack() {
        map = new HashMap<>();
    }

    /**
     * Pushes the value wrapper to the top of the stack stored under that key.
     *
     * @param keyName the key of the stack
     * @param valueWrapper the new value wrapper to push to the top of the stack stored under that key
     *
     * @throws NullPointerException if either arguments are {@code null}
     */
    public void push(String keyName, ValueWrapper valueWrapper) {
        Objects.requireNonNull(keyName, "Key name cannot be null.");
        Objects.requireNonNull(valueWrapper, "Value wrapper cannot be null.");

        MultistackEntry next = map.get(keyName);

        map.put(keyName, new MultistackEntry(valueWrapper, next));
    }

    /**
     * Removes and returns the top value of the stack stored under that key.
     *
     * @param keyName the key of the stack
     * @return the top value of the stack stored under that key
     *
     * @throws EmptyMultistackEntryException if the stack stored under that key is empty
     */
    public ValueWrapper pop(String keyName) {
        return getTopAndMaybeRemove(keyName, true);
    }

    /**
     * Returns the top value of the stack stored under that key without removing it from the stack.
     *
     * @param keyName the key of the stack
     * @return the top value of the stack stored under that key
     *
     * @throws EmptyMultistackEntryException if the stack stored under that key is empty
     */
    public ValueWrapper peek(String keyName) {
        return getTopAndMaybeRemove(keyName, false);
    }

    /**
     * Returns the top value of the stack stored under that key and optionally removes it.
     *
     * @param keyName the key of the stack
     * @param shouldRemoveHead whether it should remove the top value from the stack before returning it
     * @return the top value of the stack stored under that key
     *
     * @throws EmptyMultistackEntryException if the stack stored under that key is empty
     */
    private ValueWrapper getTopAndMaybeRemove(String keyName, boolean shouldRemoveHead) {
        Objects.requireNonNull(keyName, "Key name cannot be null.");

        if (isEmpty(keyName)) {
            throw new EmptyMultistackEntryException("Cannot get head of empty multistack entry.");
        }

        MultistackEntry top = map.get(keyName);

        // We know top is not null, since we checked if it is empty.
        if (shouldRemoveHead) {
            // If top is the only element in the stack, we can remove the key from the map.
            if (top.next == null) {
                map.remove(keyName);
            }
            else {
                map.put(keyName, top.next);
            }
        }

        return top.value;
    }

    /**
     * Returns whether the stack stored under that key is empty.
     *
     * @param keyName the key of the stack
     * @return true if it is empty,
     *         false otherwise
     */
    public boolean isEmpty(String keyName) {
        return !map.containsKey(keyName);
    }

    /**
     * Models a single linked list used to implement a stack in {@link ObjectMultistack}.
     *
     * @author Marko Lazarić
     */
    private static class MultistackEntry {

        /**
         * The next entry.
         */
        private MultistackEntry next;

        /**
         * The value stored in this entry.
         */
        private ValueWrapper value;

        /**
         * Creates a new {@link MultistackEntry} with the given arguments.
         *
         * @param value the value stored in this entry
         * @param next the next entry
         */
        private MultistackEntry(ValueWrapper value, MultistackEntry next) {
            this.value = value;
            this.next = next;
        }
    }
}