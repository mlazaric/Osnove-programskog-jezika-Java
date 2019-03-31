package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

public class Dictionary<K, V> {

	private final ArrayIndexedCollection<DictionaryEntry<K, V>> entries;

	public Dictionary() {
		this.entries = new ArrayIndexedCollection<>();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return entries.size();
	}

	public void clear() {
		entries.clear();
	}

	private DictionaryEntry<K, V> findKeyOrReturnNull(Object key) {
		if (isEmpty()) {
			return null;
		}

		ElementsGetter<DictionaryEntry<K, V>> iterator = entries.createElementsGetter();

		for (DictionaryEntry<K, V> entry = iterator.getNextElement(); ; entry = iterator.getNextElement()) {
			if (entry.key.equals(key)) {
				return entry;
			}

			if (!iterator.hasNextElement()) {
				return null;
			}
		}
	}

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

	public V get(Object key) {
		DictionaryEntry<K, V> entry = findKeyOrReturnNull(key);

		if (entry == null) {
			return null;
		}

		return entry.value;
	}

	private static class DictionaryEntry<K, V> {
		private final K key;
		private V value;

		public DictionaryEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}


	}
}
