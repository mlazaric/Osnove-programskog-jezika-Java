package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

public class ElementString extends Element {

	private final String value;

	public ElementString(String value) {
		this.value = Objects.requireNonNull(value, "Value of string element cannot be null.");
	}

	@Override
	public String asText() {
		return value;
	}

	public String getValue() {
		return value;
	}

}
