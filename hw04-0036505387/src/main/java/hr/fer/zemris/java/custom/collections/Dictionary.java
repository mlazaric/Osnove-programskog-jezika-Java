package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Models a simple dictionary class which uses an {@link ArrayIndexedCollection} to store key - value pairs.
 *
 * @author Marko Lazarić
 *
 * @param <K> type of keys
 * @param <V> type of values
 *
 */
public class Dictionary<K, V> {

	/**
	 * {@link ArrayIndexedCollection} which stores the key-value pairs.
	 */
	private final ArrayIndexedCollection<DictionaryEntry<K, V>> entries;

	/**
	 * Constructs an empty {@link Dictionary}.
	 */
	public Dictionary() {
		this.entries = new ArrayIndexedCollection<>();
	}

	/**
	 * Returns whether the {@link Dictionary} is empty.
	 *
	 * @return true if it is empty,
	 *         false otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns the number of key-value pairs stored in the {@link Dictionary}.
	 *
	 * @return the number of key-value pairs stored in the {@link Dictionary}
	 */
	public int size() {
		return entries.size();
	}

	/**
	 * Clears the {@link Dictionary}. Removes all elements from it.
	 */
	public void clear() {
		entries.clear();
	}

	/**
	 * Returns the {@link DictionaryEntry} with the specified key.
	 * If no such {@link DictionaryEntry} has been found, it returns {@code null}.
	 *
	 * @param key the key to find
	 * @return the {@link DictionaryEntry} with the specified key, or
	 *         {@code null} if no such {@link DictionaryEntry} has been found.
	 */
	private DictionaryEntry<K, V> findKeyOrReturnNull(Object key) {
		if (isEmpty()) {
			return null;
		}

		ElementsGetter<DictionaryEntry<K, V>> iterator = entries.createElementsGetter();

		for (DictionaryEntry<K, V> entry = iterator.getNextElement(); ; entry = iterator.getNextElement()) {
			if (key.equals(entry.key)) {
				return entry;
			}

			if (!iterator.hasNextElement()) {
				return null;
			}
		}
	}

	/**
	 * If the {@link Dictionary} does not contain the {@code key},
	 * the key-value pair is added to the {@link Dictionary}.
	 * Otherwise the {@link DictionaryEntry} with the specified
	 * key is set to the new {@code value}.
	 *
	 * @param key the key of the pair
	 * @param value the value of the pair
	 *
	 * @throws NullPointerException if {@code key} is {@code null}
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key, "Key cannot be null.");

		DictionaryEntry<K, V> entry = findKeyOrReturnNull(key);

		if (entry == null) {
			entries.add(new DictionaryEntry<>(key, value));
		}
		else {
			entry.value = value;
		}
	}

	/**
	 * Returns the value stored under the given key.
	 * If no such {@link DictionaryEntry} entry was found
	 * or {@code key} is {@code null}, it returns {@code null}.
	 *
	 * @param key the key to look for
	 * @return the value stored under the given key, or {@code null}
	 */
	public V get(Object key) {
		DictionaryEntry<K, V> entry = findKeyOrReturnNull(key);

		if (entry == null) {
			return null;
		}

		return entry.value;
	}

	/**
	 * Represents a single entry in the dictionary.
	 * A simple key-value pair.
	 *
	 * @author Marko Lazarić
	 *
	 * @param <K> type of the keys
	 * @param <V> type of the values
	 *
	 */
	private static class DictionaryEntry<K, V> {

		/**
		 * The key of the {@link DictionaryEntry}.<br>
		 * Cannot be changed and cannot be null.
		 */
		private final K key;

		/**
		 * The value of the {@link DictionaryEntry}.<br>
		 * Can be changed and can be null.
		 */
		private V value;

		/**
		 * Constructs a new {@link DictionaryEntry} with the given arguments.
		 *
		 * @param key the key of the entry
		 * @param value the value of the entry
		 */
		public DictionaryEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return key + "=" + value;
		}

	}
}
