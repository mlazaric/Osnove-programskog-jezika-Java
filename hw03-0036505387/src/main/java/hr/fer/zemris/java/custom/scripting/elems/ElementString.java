package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

public class ElementString extends Element {

	private final String value;

	public ElementString(String value) {
		this.value = Objects.requireNonNull(value, "Value of string element cannot be null.");
	}

	@Override
	public String asText() {
		return String.format("\"%s\"", value);
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "ElementString [value=" + value + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ElementString)) {
			return false;
		}

		ElementString other = (ElementString) obj;

		return Objects.equals(value, other.value);
	}



}
