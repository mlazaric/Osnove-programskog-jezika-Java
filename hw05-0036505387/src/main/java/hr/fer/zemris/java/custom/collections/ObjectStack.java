package hr.fer.zemris.java.custom.collections;

/**
 * A simple implementation of a stack using {@link ArrayIndexedCollection}
 * to store the objects.
 *
 * @author Marko Lazarić
 *
 * @param <E> type of elements on the stack
 *
 */
public class ObjectStack<E> {

	/**
	 * Collection used to store the objects on the stack.
	 */
	private final ArrayIndexedCollection<E> collection;

	/**
	 * Creates a new empty object stack.
	 */
	public ObjectStack() {
		collection = new ArrayIndexedCollection<>();
	}

	/**
	 * Checks if the stack is empty.
	 *
	 * @return true if the stack is empty, false otherwise
	 */
	public boolean isEmpty() {
		return collection.isEmpty();
	}

	/**
	 * Returns the number of elements on the stack.
	 *
	 * @return the number of elements on the stack
	 */
	public int size() {
		return collection.size();
	}

	/**
	 * Pushes the {@code value} to the top of the stack.
	 *
	 * @param value the object to add to the stack
	 */
	public void push(E value) {
		collection.add(value);
	}

	/**
	 * Returns the top element on the stack and removes it from the stack.
	 *
	 * @return the top element on the stack (the last one added)
	 *
	 * @throws EmptyStackException if there are no elements on the stack
	 */
	public E pop() {
		E returnValue = peek();
		int lastIndex = collection.size() - 1;

		collection.remove(lastIndex);

		return returnValue;
	}

	/**
	 * Returns the top element on the stack.
	 *
	 * @return the top element on the stack (the last one added)
	 *
	 * @throws EmptyStackException if there are no elements on the stack
	 */
	public E peek() {
		if (size() == 0) {
			throw new EmptyStackException();
		}

		int lastIndex = collection.size() - 1;
		E returnValue = collection.get(lastIndex);

		return returnValue;
	}

	/**
	 * Removes all elements from the stack.
	 */
	public void clear() {
		collection.clear();
	}

}
