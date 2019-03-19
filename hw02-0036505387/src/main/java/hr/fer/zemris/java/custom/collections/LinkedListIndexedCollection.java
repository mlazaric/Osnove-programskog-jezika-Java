package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Implements a double linked list based collection.
 *
 * @author Marko Lazarić
 *
 */
public class LinkedListIndexedCollection extends Collection {

	/**
	 * The value returned by {@link #indexOf(Object)} when it has not found the value.
	 */
	public static final int VALUE_NOT_FOUND_RETURN_VALUE = -1;

	/**
	 * The number of elements stored in the collection.
	 */
	private int size;

	/**
	 * The first element of the collection.
	 */
	private ListNode first;

	/**
	 * The last element of the collection.
	 */
	private ListNode last;

	/**
	 * Creates a new empty {@link LinkedListIndexedCollection}.
	 */
	public LinkedListIndexedCollection() {
		first = last = null;
		size = 0;
	}

	/**
	 * Creates a new {@link LinkedListIndexedCollection} and adds all the elements from the
	 * passed collection to it.
	 *
	 * @param other the collection whose elements should be added to this one
	 *
	 * @throws NullPointerException if {@code other} is {@code null}
	 */
	public LinkedListIndexedCollection(Collection other) {
		this();

		Objects.requireNonNull(other);

		this.addAll(other);
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
	 * Finds the node at the passed index.
	 *
	 * @param index the index of the node
	 * @return the node at the passed index
	 *
	 * @throws IndexOutOfBoundsException if {@code index} is not a valid index
	 */
	private ListNode findNodeByIndex(int index) {
		requireValidIndex(index);

		ListNode current;

		if (index < size / 2) {
			current = first;

			while (index != 0) {
				current = current.next;
				index--;
			}
		} else {
			current = last;
			index = size - 1 - index;

			while (index != 0) {
				current = current.prev;
				index--;
			}
		}

		return current;
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
		Object[] array = new Object[size];
		ListNode current = first;

		for (int index = 0; index < size; ++index) {
			array[index] = current.value;

			current = current.next;
		}

		return array;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.collections.Collection#forEach(hr.fer.zemris.java.custom.collections.Processor)
	 */
	@Override
	public void forEach(Processor processor) {
		for (ListNode current = first; current != null; current = current.next) {
			processor.process(current.value);
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.collections.Collection#add(java.lang.Object)
	 */
	@Override
	public void add(Object value) {
		Objects.requireNonNull(value);

		if (first == null) {
			first = last = new ListNode(null, null, value);
		} else {
			last.next = new ListNode(last, null, value);
			last = last.next;
		}

		++size;
	}

	/**
	 * Returns the object at the specified index.
	 *
	 * @param index the index of the element within the collection
	 * @return the object at the specified index
	 *
	 * @throws IndexOutOfBoundsException if {@code index} is not a valid index
	 *
	 * @see #requireValidIndex(int)
	 */
	public Object get(int index) {
		requireValidIndex(index);

		return findNodeByIndex(index).value;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.collections.Collection#clear()
	 */
	@Override
	public void clear() {
		first = last = null;

		size = 0;
	}

	/**
	 * Inserts the value at the specified position in the collection.
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
		requireValidPosition(position);

		if (position == size) {
			add(value);

			return;
		} else if (position == 0) {
			first.prev = new ListNode(null, first, value);
			first = first.prev;
		} else {
			ListNode current = findNodeByIndex(position);

			current.prev.next = new ListNode(current.prev, current, value);
			current.prev = current.prev.next;
		}

		++size;
	}

	/**
	 * Returns the index of the element in the collection. If the element is not in the
	 * collection, it returns {@link #VALUE_NOT_FOUND_RETURN_VALUE}.
	 *
	 * @param value the object to find
	 * @return the index of the object in the collection or {@link #VALUE_NOT_FOUND_RETURN_VALUE}
	 */
	public int indexOf(Object value) {
		if (value == null) {
			return VALUE_NOT_FOUND_RETURN_VALUE;
		}

		int index = 0;
		ListNode current;

		for (current = first; current != null && !value.equals(current.value); current = current.next) {
			++index;
		}

		if (current == null || !value.equals(current.value)) {
			return VALUE_NOT_FOUND_RETURN_VALUE;
		}

		return index;
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

		if (size == 1) {
			clear();

			return;
		} else if (index == 0) {
			first = first.next;

			--size;

			return;
		} else if (index == (size - 1)) {
			last = last.prev;

			--size;

			return;
		}

		ListNode toRemove = findNodeByIndex(index);

		toRemove.prev.next = toRemove.next;
		toRemove.next.prev = toRemove.prev;

		--size;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (ListNode current = first; current != null; current = current.next) {
			sb.append(current.value.toString());

			if (current.next != null) {
				sb.append(" <-> ");
			}
		}

		return sb.toString();
	}

	/**
	 * A node in the doubly linked list.
	 *
	 * @author Marko Lazarić
	 *
	 */
	private class ListNode {
		/**
		 * The previous node.
		 */
		private ListNode prev;

		/**
		 * The following node.
		 */
		private ListNode next;


		/**
		 * The object stored in the node.
		 */
		private Object value;

		/**
		 * Creates a new {@link ListNode} with the specified arguments.
		 *
		 * @param prev the previous node
		 * @param next the following node
		 * @param value the object stored in it
		 */
		public ListNode(ListNode prev, ListNode next, Object value) {
			this.prev = prev;
			this.next = next;
			this.value = value;
		}
	}
}
