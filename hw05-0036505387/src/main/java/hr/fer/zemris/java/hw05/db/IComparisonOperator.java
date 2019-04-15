package hr.fer.zemris.java.hw05.db;

/**
 * Represents a comparison operator between two {@link String}s.
 *
 * @author Marko Lazarić
 *
 */
@FunctionalInterface
public interface IComparisonOperator {

	/**
	 * Returns whether the comparison is valid.
	 *
	 * @param value1 the first string to compare
	 * @param value2 the second string to compare
	 * @return whether the comparison is valid
	 */
	boolean satisfied(String value1, String value2);

}
