package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

public class ElementVariable extends Element {

	private final String name;

	public ElementVariable(String name) {
		this.name = Objects.requireNonNull(name, "Name of variable cannot be null");
	}

	@Override
	public String asText() {
		return name;
	}

	public String getName() {
		return name;
	}

}
