package hr.fer.zemris.java.hw05.db;

/**
 * Represents a filter for {@link StudentRecord}s.
 *
 * @author Marko Lazarić
 *
 */
@FunctionalInterface
public interface IFilter{

	/**
	 * Returns whether the record passes this filter.
	 *
	 * @param record the record to test
	 * @return whether the record passes this filter
	 */
	boolean accepts(StudentRecord record);

}
