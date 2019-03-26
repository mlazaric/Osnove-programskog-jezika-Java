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

	@Override
	public String toString() {
		return "ElementVariable [name=" + name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ElementVariable)) {
			return false;
		}

		ElementVariable other = (ElementVariable) obj;

		return Objects.equals(name, other.name);
	}



}
