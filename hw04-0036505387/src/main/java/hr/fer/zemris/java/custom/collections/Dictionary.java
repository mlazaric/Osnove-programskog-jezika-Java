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

	public void put(K key, V value) {
		Objects.requireNonNull(key, "Key cannot be null.");

		int index = entries.indexOf(key);

		if (index == ArrayIndexedCollection.VALUE_NOT_FOUND_RETURN_VALUE) {
			entries.add(new DictionaryEntry<>(key, value));
		}
		else {
			entries.get(index).value = value;
		}
	}

	public V get(Object key) {
		int index = entries.indexOf(key);

		if (index == ArrayIndexedCollection.VALUE_NOT_FOUND_RETURN_VALUE) {
			return null;
		}

		return entries.get(index).value;
	}

	private static class DictionaryEntry<K, V> {
		private K key;
		private V value;

		public DictionaryEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public int hashCode() {
			return Objects.hash(key);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof DictionaryEntry)) {
				return false;
			}

			DictionaryEntry<?, ?> other = (DictionaryEntry<?, ?>) obj;

			return Objects.equals(key, other.key);
		}
	}
}
