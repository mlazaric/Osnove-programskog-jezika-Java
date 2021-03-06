package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * A node representing pure text in the syntax tree generated by {@link SmartScriptParser}.
 *
 * @author Marko Lazarić
 *
 */
public class TextNode extends Node {

	/**
	 * The text contained in this node.
	 */
	private final String text;

	/**
	 * Creates a new {@link TextNode} with the specified arguments.
	 *
	 * @param text the text of this node
	 *
	 * @throws NullPointerException if {@code text} is {@code null}
	 */
	public TextNode(String text) {
		this.text = Objects.requireNonNull(text, "Text of text node cannot be null.");
	}

	/**
	 * Returns the text of this node.
	 *
	 * @return the text of this node
	 */
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#toString()
	 */
	@Override
	public String toString() {
		return text.replace("{", "\\{").replace("\\", "\\\\");
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();

		result = prime * result + Objects.hash(text);

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
		if (!(obj instanceof TextNode)) {
			return false;
		}

		TextNode other = (TextNode) obj;

		return Objects.equals(text, other.text);
	}

}
