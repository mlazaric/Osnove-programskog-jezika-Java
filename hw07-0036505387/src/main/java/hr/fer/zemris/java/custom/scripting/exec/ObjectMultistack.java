package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ObjectMultistack {

    private Map<String, MultistackEntry> map;

    public ObjectMultistack() {
        map = new HashMap<>();
    }

    public void push(String keyName, ValueWrapper valueWrapper) {
        Objects.requireNonNull(keyName, "Key name cannot be null.");
        Objects.requireNonNull(valueWrapper, "Value wrapper cannot be null.");

        MultistackEntry next = map.get(keyName);

        map.put(keyName, new MultistackEntry(valueWrapper, next));
    }

    public ValueWrapper pop(String keyName) {
        return getTopAndMaybeRemove(keyName, true);
    }

    public ValueWrapper peek(String keyName) {
        return getTopAndMaybeRemove(keyName, false);
    }

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

    public boolean isEmpty(String keyName) {
        return !map.containsKey(keyName);
    }

    private static class MultistackEntry {
        private MultistackEntry next;
        private ValueWrapper value;

        private MultistackEntry(ValueWrapper value, MultistackEntry next) {
            this.value = value;
            this.next = next;
        }
    }
}