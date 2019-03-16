package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.Objects;

public class ArrayIndexedCollection extends Collection {
	
	private static final int VALUE_NOT_FOUND_RETURN_VALUE = -1;
	private static final int DEFAULT_CAPACITY = 16;
	
	private int size;
	private Object[] elements;
	private int capacity;
	
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			String message = "Initial capacity of an ArrayIndexedCollection cannot be less than 1";
			
			throw new IllegalArgumentException(message);
		}
		
		capacity = initialCapacity;
		elements = new Object[initialCapacity];
	}
	
	public ArrayIndexedCollection(Collection collection) {
		this(collection, DEFAULT_CAPACITY);
	}
	
	public ArrayIndexedCollection(Collection collection, int capacity) {
		this(Math.max(capacity, collection.size()));
		
		Objects.requireNonNull(collection);
		
		addAll(collection);
	}
	
	private void doubleIfNecessary() {
		if (size == capacity) {
			capacity *= 2;
			elements = Arrays.copyOf(elements, capacity);
		}
	}
	
	@Override
	public void add(Object value) {
		Objects.requireNonNull(value);
		
		doubleIfNecessary();
		
		elements[size] = value;
		++size;
	}
	
	public Object get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		return elements[index];
	}
	
	@Override
	public void clear() {
		size = 0;
		
		Arrays.fill(elements, null);
	}
	
	public void insert(Object value, int position) {
		Objects.requireNonNull(value);
		
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException();
		}
		
		doubleIfNecessary();
		
		for (int index = position; index < size; ++index) {
			elements[index + 1] = elements[index];
		}
		
		elements[position] = value;
		++size;
	}
	
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
	
	public void remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		for (int current = index; current < size; ++current) {
			elements[current] = elements[current + 1];
		}
		
		--size;
	}
	
}
