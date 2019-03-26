package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

public class ElementConstantInteger extends Element {

	private final int value;

	public ElementConstantInteger(int value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return Integer.toString(value);
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "ElementConstantInteger [value=" + value + "]";
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
		if (!(obj instanceof ElementConstantInteger)) {
			return false;
		}

		ElementConstantInteger other = (ElementConstantInteger) obj;

		return value == other.value;
	}



}
