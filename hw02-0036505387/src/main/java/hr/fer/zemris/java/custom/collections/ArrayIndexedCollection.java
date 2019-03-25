package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.Objects;

/**
 * Implements an array based collection.
 *
 * @author Marko Lazarić
 *
 */
public class ArrayIndexedCollection extends Collection {

	/**
	 * The value returned by {@link #indexOf(Object)} when it has not found the value.
	 */
	public static final int VALUE_NOT_FOUND_RETURN_VALUE = -1;

	/**
	 * The default capacity of the underlying array.
	 */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * Number of elements stored in the collection.
	 */
	private int size;

	/**
	 * An array containing all the elements in the collection.
	 */
	private Object[] elements;

	/**
	 * Creates a new {@link ArrayIndexedCollection} with the {@link #DEFAULT_CAPACITY}.
	 *
	 * @see #elements
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Creates a new {@link ArrayIndexedCollection} with the passed capacity.
	 *
	 * @param initialCapacity capacity of the underlying array
	 *
	 * @throws IllegalArgumentException if {@code initialCapacity} is less than 1
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		requireValidCapacity(initialCapacity);

		elements = new Object[initialCapacity];
	}

	/**
	 * Creates a new {@link ArrayIndexedCollection} with the {@link #DEFAULT_CAPACITY}
	 * and adds all the elements from the passed collection to it,
	 *
	 * @param collection the collection whose elements should be added to this one
	 *
	 * @throws NullPointerException if {@code collection} is {@code null}
	 */
	public ArrayIndexedCollection(Collection collection) {
		this(collection, DEFAULT_CAPACITY);
	}

	/**
	 * Creates a new {@link ArrayIndexedCollection} with the passed capacity and adds
	 * all the elements from the passed collection to it.<br/>
	 *
	 * If the passed capacity is lesser than the size of the passed collection, the
	 * size of the passed collection is used as the capacity instead.
	 *
	 * @param collection the collection whose elements should be added to this one
	 * @param capacity capacity of the underlying array
	 *
	 * @throws IllegalArgumentException if {@code capacity} is less than 1
	 * @throws NullPointerException if {@code collection} is {@code null}
	 */
	public ArrayIndexedCollection(Collection collection, int capacity) {
		requireValidCapacity(capacity);
		Objects.requireNonNull(collection);

		elements = new Object[Math.max(capacity, collection.size())];

		addAll(collection);
	}

	/**
	 * Checks whether the passed integer represents a valid capacity. A valid capacity
	 * should be greater than or equal to 1.
	 *
	 * @param capacity the integer to check
	 *
	 * @throws IllegalArgumentException if {@code capacity} is not a valid capacity (it is less than 1)
	 */
	private void requireValidCapacity(int capacity) {
		if (capacity < 1) {
			String message = "Initial capacity of an ArrayIndexedCollection cannot be less than 1";

			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Checks whether the passed integer is a valid index. A valid index should be
	 * greater than or equal to 0 and lesser than or equal to {@link #size} - 1.
	 *
	 * @param index the integer to check
	 *
	 * @throws IndexOutOfBoundsException if it is not a valid index
	 */
	private void requireValidIndex(int index) {
		if (index < 0 || index >= size) {
			String message = String.format("%d is not a valid index, size of the collection is %d", index, size);

			throw new IndexOutOfBoundsException(message);
		}
	}

	/**
	 * Checks whether the passed integer is a valid position. A valid index should be
	 * greater than or equal to 0 and lesser than or equal to {@link #size}.
	 *
	 * @param position the integer to check
	 *
	 * @throws IndexOutOfBoundsException if it is not a valid position
	 */
	private void requireValidPosition(int position) {
		if (position < 0 || position > size) {
			String message = String.format("%d is not a valid position, size of the collection is %d", position, size);

			throw new IndexOutOfBoundsException(message);
		}
	}

	/**
	 * Checks whether the underlying array is full and if it is, doubles it.
	 */
	private void doubleIfNecessary() {
		if (size == elements.length) {
			elements = Arrays.copyOf(elements, elements.length * 2);
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.collections.Collection#size()
	 */
	@Override
	public int size() {
		return size;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.collections.Collection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object value) {
		if (value == null) {
			return false;
		}

		return indexOf(value) != VALUE_NOT_FOUND_RETURN_VALUE;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.collections.Collection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);

		if (index == VALUE_NOT_FOUND_RETURN_VALUE) {
			return false;
		}

		remove(index);

		return true;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.collections.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.collections.Collection#forEach(hr.fer.zemris.java.custom.collections.Processor)
	 */
	@Override
	public void forEach(Processor processor) {
		for (int index = 0; index < size; ++index) {
			processor.process(elements[index]);
		}
	}

	/**
	 * {@inheritDoc}
	 * The average complexity of this method is amortised O(1).
	 */
	@Override
	public void add(Object value) {
		Objects.requireNonNull(value);

		doubleIfNecessary();

		elements[size] = value;
		++size;
	}

	/**
	 * Returns the object at the specified index. The average complexity of
	 * this method is O(1).
	 *
	 * @param index the index of the element within the array
	 * @return the object at the specified index
	 *
	 * @throws IndexOutOfBoundsException if {@code index} is not a valid index
	 *
	 * @see #requireValidIndex(int)
	 */
	public Object get(int index) {
		requireValidIndex(index);

		return elements[index];
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.collections.Collection#clear()
	 */
	@Override
	public void clear() {
		size = 0;

		Arrays.fill(elements, null);
	}

	/**
	 * Inserts the value at the specified position in the collection. The
	 * average complexity of this method is O(n).
	 *
	 * @param value the value to insert
	 * @param position the position to insert at
	 *
	 * @throws NullPointerException if {@code value} is {@code null}
	 * @throws IndexOutOfBoundsException if {@code position} is not a valid position
	 *
	 * @see #requireValidPosition(int)
	 */
	public void insert(Object value, int position) {
		Objects.requireNonNull(value);
		requireValidPosition(position);

		doubleIfNecessary();

		for (int index = size + 1; index > position; --index) {
			elements[index] = elements[index - 1];
		}

		elements[position] = value;
		++size;
	}

	/**
	 * Returns the index of the element in the collection. If the element is not in the
	 * collection, it returns {@link #VALUE_NOT_FOUND_RETURN_VALUE}. The average complexity
	 * of this method is O(n).
	 *
	 * @param value the object to find
	 * @return the index of the object in the collection or {@link #VALUE_NOT_FOUND_RETURN_VALUE}
	 */
	public int indexOf(Object value) {
		if (value == null) {
			return VALUE_NOT_FOUND_RETURN_VALUE;
		}

		for (int index = 0; index < size; ++index) {
			if (value.equals(elements[index])) {
				return index;
			}
		}

		return VALUE_NOT_FOUND_RETURN_VALUE;
	}

	/**
	 * Removes the element at the specified index from the collection.
	 *
	 * @param index the index of the element
	 *
	 * @throws IndexOutOfBoundsException if {@code index} is not a valid index
	 *
	 * @see #requireValidIndex(int)
	 */
	public void remove(int index) {
		requireValidIndex(index);

		for (int current = index; current < size; ++current) {
			elements[current] = elements[current + 1];
		}

		elements[size - 1] = null;

		--size;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Arrays.toString(elements);
	}

}
