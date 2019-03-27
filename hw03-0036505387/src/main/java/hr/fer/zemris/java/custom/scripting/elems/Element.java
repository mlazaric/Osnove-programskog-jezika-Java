package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents an element of an expression for the Smart scripting language.
 *
 * @author Marko Lazarić
 *
 */
public abstract class Element {

	/**
	 * Return the textual representation of this element.
	 * The returned result should be parseable.
	 *
	 * @return the textual representation of this element
	 */
	public String asText() {
		return "";
	}

}
