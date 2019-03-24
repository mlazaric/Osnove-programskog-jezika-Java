package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

public class TextNode extends Node {

	private final String text;

	public TextNode(String text) {
		this.text = Objects.requireNonNull(text, "Text of text node cannot be null.");
	}

	public String getText() {
		return text;
	}

}
