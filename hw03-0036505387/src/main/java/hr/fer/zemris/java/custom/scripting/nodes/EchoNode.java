package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;

public class EchoNode extends Node {

	private final Element[] elements;

	public EchoNode(Element[] elements) {
		this.elements = Objects.requireNonNull(elements, "Elements of echo node cannot be null");
	}

	public Element[] getElements() {
		return elements;
	}

}
