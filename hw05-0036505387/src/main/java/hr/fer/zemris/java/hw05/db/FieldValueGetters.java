package hr.fer.zemris.java.hw05.db;

/**
 * Contains most commonly used {@link IFieldValueGetter}s.
 *
 * @author Marko Lazarić
 *
 */
public final class FieldValueGetters {

	/**
	 * This class should not be instanced.
	 */
	private FieldValueGetters() {}

	/**
	 * Returns the first name of that {@link StudentRecord}.
	 *
	 * @see {@link StudentRecord#getFirstName()}
	 */
	public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;

	/**
	 * Returns the last name of that {@link StudentRecord}.
	 *
	 * @see {@link StudentRecord#getLastName()}
	 */
	public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;

	/**
	 * Returns the JMBAG of that {@link StudentRecord}.
	 *
	 * @see {@link StudentRecord#getJmbag()}
	 */
	public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;

}
