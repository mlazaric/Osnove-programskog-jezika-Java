package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.util.Arrays;
import java.util.Objects;

/**
 * A node representing an echo tag ("{$=") in the syntax tree generated by {@link SmartScriptParser}.
 *
 * @author Marko Lazarić
 *
 */
public class EchoNode extends Node {

	/**
	 * The elements of this node.
	 */
	private final Element[] elements;

	/**
	 * Creates a new {@link EchoNode} with the specified elements.
	 *
	 * @param elements the elements of this {@link EchoNode}
	 *
	 * @throws NullPointerException if {@code elements} is {@code null}
	 */
	public EchoNode(Element[] elements) {
		this.elements = Objects.requireNonNull(elements, "Elements of echo node cannot be null");
	}

	/**
	 * Returns the elements of this node.
	 *
	 * @return the elements of this node
	 */
	public Element[] getElements() {
		return elements;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{$=");

		for (Element element : elements) {
			sb.append(element.toString()).append(' ');
		}

		sb.append("$}");

		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();

		result = prime * result + Arrays.hashCode(elements);

		return result;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof EchoNode)) {
			return false;
		}

		EchoNode other = (EchoNode) obj;

		return Arrays.equals(elements, other.elements);
	}

	@Override
	public void accept(INodeVisitor iNodeVisitor) {
		iNodeVisitor.visitEchoNode(this);
	}
}
