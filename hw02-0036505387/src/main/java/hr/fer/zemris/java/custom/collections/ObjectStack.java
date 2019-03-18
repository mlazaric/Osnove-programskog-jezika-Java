package hr.fer.zemris.java.custom.collections;

public class ObjectStack {

	private final ArrayIndexedCollection collection;

	public ObjectStack() {
		collection = new ArrayIndexedCollection();
	}

	public boolean isEmpty() {
		return collection.isEmpty();
	}

	public int size() {
		return collection.size();
	}

	public void push(Object value) {
		collection.add(value);
	}

	public Object pop() {
		Object returnValue = peek();
		int lastIndex = collection.size() - 1;

		collection.remove(lastIndex);

		return returnValue;
	}

	public Object peek() {
		if (size() == 0) {
			throw new EmptyStackException();
		}

		int lastIndex = collection.size() - 1;
		Object returnValue = collection.get(lastIndex);

		return returnValue;
	}

	public void clear() {
		collection.clear();
	}

}
