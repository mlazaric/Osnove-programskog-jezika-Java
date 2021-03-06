package hr.fer.zemris.java.custom.collections;

/**
 * A simple implementation of a stack using {@link ArrayIndexedCollection}
 * to store the objects.
 *
 * @author Marko Lazarić
 *
 */
public class ObjectStack {

	/**
	 * Collection used to store the objects on the stack.
	 */
	private final ArrayIndexedCollection collection;

	/**
	 * Creates a new empty object stack.
	 */
	public ObjectStack() {
		collection = new ArrayIndexedCollection();
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
	public void push(Object value) {
		collection.add(value);
	}

	/**
	 * Returns the top element on the stack and removes it from the stack.
	 *
	 * @return the top element on the stack (the last one added)
	 *
	 * @throws EmptyStackException if there are no elements on the stack
	 */
	public Object pop() {
		Object returnValue = peek();
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
	public Object peek() {
		if (size() == 0) {
			throw new EmptyStackException();
		}

		int lastIndex = collection.size() - 1;
		Object returnValue = collection.get(lastIndex);

		return returnValue;
	}

	/**
	 * Removes all elements from the stack.
	 */
	public void clear() {
		collection.clear();
	}

}
