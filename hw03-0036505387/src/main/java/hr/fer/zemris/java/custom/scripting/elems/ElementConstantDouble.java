package hr.fer.zemris.java.custom.scripting.elems;

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

}
