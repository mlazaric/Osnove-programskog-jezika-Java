package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents a variable name in an expression for the Smart scripting language.
 *
 * @author Marko Lazarić
 *
 */
public class ElementVariable extends Element {

	/**
	 * The name of the variable.
	 */
	private final String name;

	/**
	 * Creates a new {@link ElementVariable} with the given name.
	 *
	 * @param name the name of the variable
	 *
	 * @throws NullPointerException if {@code name} is {@code null}
	 */
	public ElementVariable(String name) {
		this.name = Objects.requireNonNull(name, "Name of variable cannot be null");
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.elems.Element#asText()
	 */
	@Override
	public String asText() {
		return name;
	}

	/**
	 * Returns the name of the variable.
	 *
	 * @return the name of the variable
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ElementVariable [name=" + name + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
