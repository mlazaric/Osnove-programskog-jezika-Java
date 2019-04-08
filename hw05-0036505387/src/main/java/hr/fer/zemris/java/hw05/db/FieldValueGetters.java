package hr.fer.zemris.java.hw05.db;

public final class FieldValueGetters {

	private FieldValueGetters() {}

	public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;
	public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;
	public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;

}
