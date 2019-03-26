package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

public abstract class Node {

	protected ArrayIndexedCollection collection;

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

	@Override
	public String toString() {
		return "Node [collection=" + collection + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(collection);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Node)) {
			return false;
		}

		Node other = (Node) obj;

		return Objects.equals(collection, other.collection);
	}



}
