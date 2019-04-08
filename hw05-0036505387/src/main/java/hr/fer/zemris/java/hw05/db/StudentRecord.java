package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

public class StudentRecord {

	private static final int MAX_GRADE = 5;
	private static final int MIN_GRADE = 1;

	private final String jmbag;
	private final String firstName;
	private final String lastName;
	private final int finalGrade;

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
	 * @return the jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the finalGrade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}

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

	@Override
	public String toString() {
		return "StudentRecord [jmbag=" + jmbag + ", firstName=" + firstName + ", lastName=" + lastName + ", finalGrade="
				+ finalGrade + "]";
	}

}
