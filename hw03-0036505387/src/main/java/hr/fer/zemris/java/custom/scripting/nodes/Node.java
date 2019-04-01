package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Represents a node in the syntax tree built by {@link SmartScriptParser}.
 *
 * @author Marko Lazarić
 *
 */
public abstract class Node {

	/**
	 * The children nodes of this node.
	 */
	protected ArrayIndexedCollection children;

	/**
	 * Adds a new child to this node.
	 *
	 * @param child the new child node
	 *
	 * @throws NullPointerException if {@code child} is {@code null}
	 */
	public void addChildNode(Node child) {
		Objects.requireNonNull(child, "Child node cannot be null.");

		if (children == null) {
			children = new ArrayIndexedCollection();
		}

		children.add(child);
	}

	/**
	 * Returns the number of children this node has.
	 *
	 * @return the number of children this node has
	 */
	public int numberOfChildren() {
		if (children == null) {
			return 0;
		}

		return children.size();
	}

	/**
	 * Returns the child at the specified index in the collection of children.
	 *
	 * @param index the index of the child in the collection
	 * @return the child node
	 *
	 * @throws IndexOutOfBoundsException if {@code index} is not a valid index
	 */
	public Node getChild(int index) {
		if (children == null) {
			throw new IndexOutOfBoundsException("Node has no children.");
		}

		if (index < 0 || index >= numberOfChildren()) {
			throw new IndexOutOfBoundsException("Invalid index for node children.");
		}

		return (Node) children.get(index);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (ElementsGetter eg = children.createElementsGetter(); eg.hasNextElement();) {
			sb.append(eg.getNextElement().toString());
		}

		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(children);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

		return Objects.equals(children, other.children);
	}

}
