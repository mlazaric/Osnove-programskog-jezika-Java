package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Models a single student represented by its jmbag, first name, last name and final grade.
 *
 * @author Marko Lazarić
 *
 */
public class StudentRecord {

	/**
	 * The maximum possible grade.
	 */
	private static final int MAX_GRADE = 5;

	/**
	 * The minimum possible grade.
	 */
	private static final int MIN_GRADE = 1;

	/**
	 * The student's jmbag.
	 */
	private final String jmbag;

	/**
	 * The student's first name.
	 */
	private final String firstName;

	/**
	 * The student's last name.
	 */
	private final String lastName;

	/**
	 * The student's final grade.
	 */
	private final int finalGrade;

	/**
	 * Creates a new {@link StudentRecord} with the given arguments.
	 *
	 * @param jmbag the student's jmbag
	 * @param lastName the student's last name
	 * @param firstName the student's first name
	 * @param finalGrade the student's final grade
	 *
	 * @throws NullPointerException if any of the first three arguments is null
	 * @throws IllegalArgumentException if finalGrade is not a valid grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		this.jmbag = Objects.requireNonNull(jmbag, "JMBAG cannot be null.");
		this.firstName = Objects.requireNonNull(firstName, "First name cannot be null.");
		this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null.");

		if (finalGrade < MIN_GRADE || finalGrade > MAX_GRADE) {
			throw new IllegalArgumentException("'" + finalGrade + "' is not a valid grade.");
		}

		this.finalGrade = finalGrade;
	}

	/**
	 * Returns the student's jmbag.
	 *
	 * @return the student's jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Returns the student's first name.
	 *
	 * @return the student's first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns the student's last name.
	 *
	 * @return the student's last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns the student's final grade.
	 *
	 * @return the student's final grade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
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
		if (!(obj instanceof StudentRecord)) {
			return false;
		}

		StudentRecord other = (StudentRecord) obj;

		return Objects.equals(jmbag, other.jmbag);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "StudentRecord [jmbag=" + jmbag + ", firstName=" + firstName + ", lastName=" + lastName + ", finalGrade="
				+ finalGrade + "]";
	}

}
