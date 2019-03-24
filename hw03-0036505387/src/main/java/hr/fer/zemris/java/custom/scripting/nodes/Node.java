package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

public class Node {

	private ArrayIndexedCollection collection;

	public void addChildNode(Node child) {
		Objects.requireNonNull(child, "Child node cannot be null.");

		if (collection == null) {
			collection = new ArrayIndexedCollection();
		}

		collection.add(child);
	}

	public int numberOfChildren() {
		if (collection == null) {
			return 0;
		}

		return collection.size();
	}

	public Node getChild(int index) {
		if (collection == null) {
			throw new IndexOutOfBoundsException("Node has no children.");
		}

		if (index < 0 || index >= numberOfChildren()) {
			throw new IndexOutOfBoundsException("Invalid index for node children.");
		}

		return (Node) collection.get(index);
	}

}
