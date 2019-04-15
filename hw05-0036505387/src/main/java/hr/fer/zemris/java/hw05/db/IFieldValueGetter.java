package hr.fer.zemris.java.hw05.db;

/**
 * Represents a field value getter for {@link StudentRecord} fields.
 *
 * @author Marko Lazarić
 *
 */
@FunctionalInterface
public interface IFieldValueGetter {

	/**
	 * Returns the value of that {@link StudentRecord} field.
	 *
	 * @param record the record whose field value it should return
	 * @return the value of that {@link StudentRecord} field
	 */
	String get(StudentRecord record);

}
