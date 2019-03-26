package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{$=");

		for (Element element : elements) {
			sb.append(element.asText()).append(' ');
		}

		sb.append("$}");

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();

		result = prime * result + Arrays.hashCode(elements);

		return result;
	}

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


}
