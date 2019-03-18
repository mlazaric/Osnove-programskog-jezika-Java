package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

public class LinkedListIndexedCollection extends Collection {

	public static final int VALUE_NOT_FOUND_RETURN_VALUE = -1;

	private int size;
	private ListNode first;
	private ListNode last;

	public LinkedListIndexedCollection() {
		first = last = null;
		size = 0;
	}

	public LinkedListIndexedCollection(Collection other) {
		this();

		this.addAll(other);
	}

	private void requireValidIndex(int index) {
		if (index < 0 || index >= size) {
			String message = String.format("%d is not a valid index, size of the collection is %d", index, size);

			throw new IndexOutOfBoundsException(message);
		}
	}

	private void requireValidPosition(int position) {
		if (position < 0 || position > size) {
			String message = String.format("%d is not a valid position, size of the collection is %d", position, size);

			throw new IndexOutOfBoundsException(message);
		}
	}

	private ListNode findNodeByIndex(int index) {
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

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean contains(Object value) {
		return indexOf(value) != VALUE_NOT_FOUND_RETURN_VALUE;
	}

	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);

		if (index == VALUE_NOT_FOUND_RETURN_VALUE) {
			return false;
		}

		remove(index);

		return true;
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		ListNode current = first;

		for (int index = 0; index < size; ++index) {
			System.out.println(index + " " + current);
			array[index] = current.value;

			current = current.next;
		}

		return array;
	}

	@Override
	public void forEach(Processor processor) {
		for (ListNode current = first; current != null; current = current.next) {
			processor.process(current.value);
		}
	}

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

	public Object get(int index) {
		requireValidIndex(index);

		return findNodeByIndex(index).value;
	}

	@Override
	public void clear() {
		first = last = null;

		size = 0;
	}

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

	private class ListNode {
		private ListNode prev, next;
		private Object value;

		public ListNode(ListNode prev, ListNode next, Object value) {
			this.prev = prev;
			this.next = next;
			this.value = value;
		}
	}
}
