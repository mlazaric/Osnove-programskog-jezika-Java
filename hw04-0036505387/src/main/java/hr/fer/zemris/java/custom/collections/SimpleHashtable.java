package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.Objects;

public class SimpleHashtable<K, V> {

	public static final int MINIMUM_CAPACITY = 1;
	public static final int DEFAULT_CAPACITY = 16;
	private static final double CUTOFF_POINT = 0.75;

	private TableEntry<K, V>[] table;
	private int size;

	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < MINIMUM_CAPACITY) {
			throw new IllegalArgumentException("Number of slots must be greater than 0.");
		}

		this.table = new TableEntry[greaterThanOrEqualPowerOf2(capacity)];
	}

	private int greaterThanOrEqualPowerOf2(int number) {
		int current = 1;

		while (number > current) {
			current *= 2;
		}

		return current;
	}

	private int getSlotFromKey(Object key) {
		Objects.requireNonNull(key, "Key cannot be null.");

		return Math.abs(key.hashCode()) % table.length;
	}

	private TableEntry<K, V> findKey(Object key) {
		int slot = getSlotFromKey(key);
		TableEntry<K, V> prev = null;

		for (TableEntry<K, V> current = table[slot]; current != null; prev = current, current = current.next) {
			if (current.key.equals(key)) {
				return current;
			}
		}

		return prev;
	}

	private TableEntry<K, V> findValue(Object value) {
		for (TableEntry<K, V> slot : table) {
			for (TableEntry<K, V> current = slot; current != null; current = current.next) {
				if (Objects.equals(value, current.value)) {
					return current;
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private void doubleIfNecessary() {
		double percentFilled = size() / (double) table.length;

		if (percentFilled >= CUTOFF_POINT) {
			TableEntry<K, V>[] oldTable = table;
			table = new TableEntry[this.table.length * 2];

			for (TableEntry<K, V> firstInSlot : oldTable) {
				for (TableEntry<K, V> current = firstInSlot; current != null; current = current.next) {
					int slot = getSlotFromKey(current.key);

					current.next = table[slot];
					table[slot] = current;
				}
			}
		}
	}

	public void put(K key, V value) {
		Objects.requireNonNull(key, "Key cannot be null.");

		if (slotIsEmpty(key)) { // If the slot is empty, we know it doesn't contain the specified key
			table[getSlotFromKey(key)] = new TableEntry<>(key, value);
			++size;
		}
		else {
			TableEntry<K, V> entry = findKey(key);

			if (entry.key.equals(key)) { // Update the entry's value if we found an entry with the specified key
				entry.value = value;
			}
			else { // Add a new entry if we haven't found such an entry
				entry.next = new TableEntry<>(key, value);
				++size;
			}
		}
	}

	private boolean slotIsEmpty(Object key) {
		return table[getSlotFromKey(key)] == null;
	}

	public V get(Object key) {
		if (key == null) {
			return null;
		}

		if (slotIsEmpty(key)) {
			return null;
		}

		TableEntry<K, V> entry = findKey(key);

		if (entry.key.equals(key)) {
			return entry.value;
		}

		return null;
	}

	public int size() {
		return size;
	}

	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}

		if (slotIsEmpty(key)) {
			return false;
		}

		return findKey(key).key.equals(key);
	}

	public boolean containsValue(Object value) {
		return findValue(value) != null;
	}

	public void remove(Object key) {
		// If the key doesn't exist in the table, we have nothing to remove
		if (!containsKey(key)) {
			return;
		}

		int slot = getSlotFromKey(key);
		TableEntry<K, V> current = table[slot];

		// If it's the first in its slot, we should update it in the table variable
		if (current.key.equals(key)) {
			table[slot] = current.next;
			--size;

			return;
		}

		// Otherwise we find the entry before the one with the given key
		for (; current != null; current = current.next) {
			if (current.next != null && current.next.key.equals(key)) {
				break;
			}
		}

		// current will never be null, since we already checked if it contains the key
		current.next = current.next.next;
		--size;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append('[');

		for (TableEntry<K, V> slot : table) {
			for (TableEntry<K, V> current = slot; current != null; current = current.next) {
				sb.append(slot.toString()).append(", ");
			}
		}

		sb.append(']');

		return sb.toString();
	}

	public void clear() {
		Arrays.fill(table, null);
	}

	public int getCapacity() {
		return table.length;
	}

	private static class TableEntry<K, V> {

		private final K key;
		private V value;
		private TableEntry<K, V> next;

		public TableEntry(K key, V value) {
			this.key = Objects.requireNonNull(key, "Key of TableEntry cannot be null.");
			this.value = value;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		@Override
		public String toString() {
			return "key=" + key + ", value=" + value + "";
		}
	}
}
