package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

public class ElementConstantDouble extends Element {

	private final double value;

	public ElementConstantDouble(double value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return Double.toString(value);
	}

	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "ElementConstantDouble [value=" + value + "]";
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
		if (!(obj instanceof ElementConstantDouble)) {
			return false;
		}

		ElementConstantDouble other = (ElementConstantDouble) obj;

		return Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
	}



}
