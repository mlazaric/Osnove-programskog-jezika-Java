package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import hr.fer.zemris.java.custom.collections.SimpleHashtable.TableEntry;

/**
 * Models a simple hash table which efficiently stores key-value pairs.
 *
 * @author Marko Lazarić
 *
 * @param <K> type of keys
 * @param <V> type of values
 */
public class SimpleHashtable<K, V> implements Iterable<TableEntry<K, V>>{

	/**
	 * Minimum permissible number of slots in the table.
	 */
	public static final int MINIMUM_CAPACITY = 1;

	/**
	 * Default number of slots in the table.
	 */
	public static final int DEFAULT_CAPACITY = 16;

	/**
	 * Percentage of slots filled which causes the number of slots to double,
	 * to maintain efficient lookup time.
	 */
	private static final double CUTOFF_POINT = 0.75;

	/**
	 * Slots of the hash table.
	 */
	private TableEntry<K, V>[] table;

	/**
	 * Number of key-value pairs stored in the hash table.
	 */
	private int size;

	/**
	 * Number of slots which have at least one key-value pair in them.
	 */
	private int numberOfFilledSlots;

	/**
	 * Number of modifications of this table, used by {@link IteratorImpl}.
	 */
	private long modificationCount;

	/**
	 * Creates a new {@link SimpleHashtable} with the {@link #DEFAULT_CAPACITY}.
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Creates a new {@link SimpleHashtable} with the given capacity.
	 *
	 * @param capacity the number of slots
	 *
	 * @throws IllegalArgumentException if {@code capacity} is less than {@link #MINIMUM_CAPACITY}
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < MINIMUM_CAPACITY) {
			throw new IllegalArgumentException("Number of slots must be greater than 0.");
		}

		this.table = new TableEntry[greaterThanOrEqualPowerOf2(capacity)];
	}

	/**
	 * Returns a power of 2 which is equal to {@code number} if it is already a power of 2,
	 * or the first power of 2 which is greater than {@code number}.
	 *
	 * @param number the number whose closest power of 2 we want
	 * @return greater than or equal power of 2
	 */
	private int greaterThanOrEqualPowerOf2(int number) {
		int current = 1;

		while (number > current) {
			current *= 2;
		}

		return current;
	}

	/**
	 * Calculates the slot from the key argument.
	 *
	 * @param key the key of the key-value pair
	 * @return which slot it belongs in
	 *
	 * @throws NullPointerException if {@code key} is {@code null}
	 */
	private int getSlotFromKey(Object key) {
		Objects.requireNonNull(key, "Key cannot be null.");

		return Math.abs(key.hashCode()) % table.length;
	}

	/**
	 * Finds the {@link TableEntry} containing the specified key or the final
	 * {@link TableEntry} of the slot in which the {@code key} belongs.
	 *
	 * @param key the key to find
	 * @return {@link TableEntry} with the specified key or the final {@link TableEntry}
	 *         of the slot in which the {@code key} belongs
	 */
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

	/**
	 * Finds the first {@link TableEntry} containing the specified value.
	 *
	 * @param value the value to find
	 * @return the first {@link TableEntry} with the {@code value} or {@code null}
	 *         if no such {@link TableEntry} has been found
	 */
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

	/**
	 * Doubles the number of slots in the table if {@link #CUTOFF_POINT} has been reached.
	 */
	@SuppressWarnings("unchecked")
	private void doubleIfNecessary() {
		double percentFilled = numberOfFilledSlots / (double) table.length;

		if (percentFilled >= CUTOFF_POINT) {
			TableEntry<K, V>[] oldTable = table;
			table = new TableEntry[this.table.length * 2];
			numberOfFilledSlots = 0;

			for (TableEntry<K, V> firstInSlot : oldTable) {
				for (TableEntry<K, V> current = firstInSlot; current != null; current = current.next) {
					int slot = getSlotFromKey(current.key);

					if (table[slot] == null) {
						++numberOfFilledSlots;
					}

					current.next = table[slot];
					table[slot] = current;
				}
			}

			modificationCount++;
		}
	}

	/**
	 * Adds a new key-value pair to the table if there are no {@link TableEntry}s with the specified key.
	 * Otherwise it updates the existing {@link TableEntry}'s value.
	 *
	 * @param key the key of the key-value pair
	 * @param value the value of the key-value pair
	 *
	 * @throws NullPointerException if {@code key} is {@code null}
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key, "Key cannot be null.");

		if (slotIsEmpty(key)) { // If the slot is empty, we know it doesn't contain the specified key
			table[getSlotFromKey(key)] = new TableEntry<>(key, value);
			++size;
			++numberOfFilledSlots;
			modificationCount++;
		}
		else {
			TableEntry<K, V> entry = findKey(key);

			if (entry.key.equals(key)) { // Update the entry's value if we found an entry with the specified key
				entry.value = value;
			}
			else { // Add a new entry if we haven't found such an entry
				entry.next = new TableEntry<>(key, value);
				++size;
				modificationCount++;
			}
		}

		doubleIfNecessary();
	}

	/**
	 * Returns whether the slot {@code key} belongs in is empty.
	 *
	 * @param key the key whose slot we should check
	 * @return true if it is empty,
	 *         false otherwise
	 */
	private boolean slotIsEmpty(Object key) {
		return table[getSlotFromKey(key)] == null;
	}

	/**
	 * Returns the value associated with the given key.
	 * If no such value was found, {@code null} is returned.
	 *
	 * @param key the key whose value it should return
	 * @return the value associated with the given key or {@code null}
	 */
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

	/**
	 * Returns the number of key-value pairs stored in the table.
	 *
	 * @return the number of key-value pairs stored in the table
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns whether the table contains the specified key.
	 *
	 * @param key the key to look for
	 * @return true if the table contains it,
	 *         false otherwise
	 */
	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}

		if (slotIsEmpty(key)) {
			return false;
		}

		return findKey(key).key.equals(key);
	}

	/**
	 * Returns whether the table contains the specified value.
	 *
	 * @param value the value to look for
	 * @return true if the table contains it,
	 *         false otherwise
	 */
	public boolean containsValue(Object value) {
		return findValue(value) != null;
	}

	/**
	 * Removes the element associated with the given key.
	 *
	 * @param key the key whose key-value pair we should remove from the table
	 */
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
			modificationCount++;

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
		modificationCount++;
	}

	/**
	 * Returns whether the table is empty.
	 *
	 * @return true if the table is empty,
	 *         false otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		boolean first = true;

		sb.append('[');

		for (TableEntry<K, V> slot : table) {
			for (TableEntry<K, V> current = slot; current != null; current = current.next) {
				if (!first) {
					sb.append(", ");
				}
				else {
					first = false;
				}

				sb.append(slot.toString());
			}
		}

		sb.append(']');

		return sb.toString();
	}

	/**
	 * Removes all key-value pairs from the table.
	 */
	public void clear() {
		Arrays.fill(table, null);
		size = 0;
		modificationCount++;
	}

	/**
	 * Returns the number of slots in the table. Used for testing.
	 *
	 * @return the number of slots in the table
	 */
	public int getCapacity() {
		return table.length;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Represents an entry in the {@link SimpleHashtable}.
	 *
	 * @author Marko Lazarić
	 *
	 * @param <K> type of keys
	 * @param <V> type of values
	 */
	protected static class TableEntry<K, V> {

		/**
		 * The key of this key-value pair.
		 */
		private final K key;

		/**
		 * The value of this key-value pair.
		 */
		private V value;

		/**
		 * The next element in this slot.
		 */
		private TableEntry<K, V> next;

		/**
		 * Creates a new {@link TableEntry} with the given key and value.
		 *
		 * @param key the key of the key-value pair
		 * @param value the value of the key-value pair
		 *
		 * @throws NullPointerException if {@code key} is {@code null}
		 */
		public TableEntry(K key, V value) {
			this.key = Objects.requireNonNull(key, "Key of TableEntry cannot be null.");
			this.value = value;
		}

		/**
		 * Returns the value of this key-value pair.
		 *
		 * @return the value of this key-value pair
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets the value of this key-value pair.
		 *
		 * @param value the new value for this key-value pair
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * Returns the key of this key-value pair.
		 *
		 * @return the key of this key-value pair
		 */
		public K getKey() {
			return key;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return key + "=" + value;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return Objects.hash(key, value);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals()
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof TableEntry)) {
				return false;
			}

			TableEntry<?, ?> other = (TableEntry<?, ?>) obj;

			return Objects.equals(key, other.key) && Objects.equals(value, other.value);
		}
	}

	/**
	 * Iterator over {@link SimpleHashtable}.
	 *
	 * @author Marko Lazarić
	 *
	 */
	private class IteratorImpl implements Iterator<TableEntry<K,V>> {
		/**
		 * Number of modifications made to the table, used to make sure the
		 * table has not been modified while being iterated over.
		 */
		private long savedModificationCount;

		/**
		 * Index of the current slot.
		 */
		private int currentSlot;

		/**
		 * {@link TableEntry} which {@link #next()} will return.
		 */
		private TableEntry<K, V> nextEntry;

		/**
		 * The last {@link TableEntry} {@link #next()} has returned.
		 */
		private TableEntry<K, V> currentEntry;

		/**
		 * Creates a new {@link IteratorImpl}.
		 */
		private IteratorImpl() {
			savedModificationCount = modificationCount;

			currentSlot = -1; // findNextElement will increment it
			findNextElement();
		}

		/* (non-Javadoc)
		 * @see java.lang.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			checkIfItHasBeenModified();

			return nextEntry != null;
		}

		/* (non-Javadoc)
		 * @see java.lang.Iterator#next()
		 */
		@Override
		public TableEntry<K, V> next() {
			checkIfItHasBeenModified();

			if (nextEntry == null) {
				throw new NoSuchElementException("No more elements left to iterate over in SimpleHashtable.");
			}

			currentEntry = nextEntry;

			findNextElement();

			return currentEntry;
		}

		/* (non-Javadoc)
		 * @see java.lang.Iterator#remove()
		 */
		@Override
		public void remove() {
			checkIfItHasBeenModified();

			if (currentEntry == null) {
				throw new IllegalStateException("Cannot remove the same element again.");
			}

			SimpleHashtable.this.remove(currentEntry.key);

			currentEntry = null;
			savedModificationCount = modificationCount;
		}

		/**
		 * Check if {@link #savedModificationCount} is equal to {@link SimpleHashtable#modificationCount}.
		 *
		 * @throws ConcurrentModificationException if they are not equal
		 */
		private void checkIfItHasBeenModified() {
			if (savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException("SimpleHashtable was modified while being iterated over.");
			}
		}

		/**
		 * Find the next element {@link #next()} will return.
		 */
		private void findNextElement() {
			if (nextEntry == null) { // Empty slot or end of this slot
				while (nextEntry == null) {
					++currentSlot;

					if (currentSlot >= table.length) {
						break;
					}

					nextEntry = table[currentSlot];
				}
			}
			else {
				nextEntry = nextEntry.next;

				if (nextEntry == null) { // We have reached the end of this slot
					findNextElement();
				}
			}
		}
	}

}
