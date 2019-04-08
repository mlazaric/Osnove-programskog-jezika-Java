package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FieldValueGettersTest {

	private static final String JMBAG = "0000000057";
	private static final String LAST_NAME = "Širanović";
	private static final String FIRST_NAME = "Hrvoje";
	private static final int GRADE = 2;

	private static StudentRecord getRecord() {
		return new StudentRecord(JMBAG, LAST_NAME, FIRST_NAME, GRADE);
	}

	@Test
	void testJmbagFieldValueGetter() {
		assertEquals(JMBAG, FieldValueGetters.JMBAG.get(getRecord()));
	}

	@Test
	void testFirstNameFieldValueGetter() {
		assertEquals(FIRST_NAME, FieldValueGetters.FIRST_NAME.get(getRecord()));
	}

	@Test
	void testLastNameFieldValueGetter() {
		assertEquals(LAST_NAME, FieldValueGetters.LAST_NAME.get(getRecord()));
	}

}
