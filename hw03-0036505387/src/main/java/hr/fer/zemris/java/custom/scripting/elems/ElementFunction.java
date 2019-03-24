package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

public class ElementFunction extends Element {

	private final String name;

	public ElementFunction(String name) {
		this.name = Objects.requireNonNull(name, "Name of function cannot be null.");
	}

	@Override
	public String asText() {
		return name;
	}

	public String getName() {
		return name;
	}

}
