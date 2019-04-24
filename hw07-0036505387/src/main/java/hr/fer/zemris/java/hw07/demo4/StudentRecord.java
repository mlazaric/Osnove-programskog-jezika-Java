package hr.fer.zemris.java.hw07.demo4;

import java.util.Objects;

/**
 * Record representing a student enrolled in the class.
 *
 * @author Marko Lazarić
 */
public class StudentRecord {

    /**
     * The student's JMBAG.
     */
    private final String jmbag;

    /**
     * The student's last name.
     */
    private final String lastName;

    /**
     * The student's first name.
     */
    private final String firstName;

    /**
     * The number of points the student has earned on the midterm.
     */
    private final double pointsOnMidterm;

    /**
     * The number of points the student has earned on the final.
     */
    private final double pointsOnFinal;

    /**
     * The number of points the student has earned from exercises.
     */
    private final double pointsFromExercises;

    /**
     * The final grade the student has earned.
     */
    private final int grade;

    /**
     * Creates a new {@link StudentRecord} with the given arguments.
     *
     * @param jmbag the student's JMBAG
     * @param lastName the student's last name
     * @param firstName the student's first name
     * @param pointsOnMidterm the number of points they have earned on the midterm
     * @param pointsOnFinal the number of points they have earned on the final
     * @param pointsFromExercises the number of points they have earned from exercises
     * @param grade the final grade the student has earned
     *
     * @throws NullPointerException if jmbag, lastName or firstName is null
     */
    public StudentRecord(String jmbag, String lastName, String firstName, double pointsOnMidterm, double pointsOnFinal, double pointsFromExercises, int grade) {
        this.jmbag = Objects.requireNonNull(jmbag, "JMBAG cannot be null.");
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null.");
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null.");
        this.pointsOnMidterm = pointsOnMidterm;
        this.pointsOnFinal = pointsOnFinal;
        this.pointsFromExercises = pointsFromExercises;
        this.grade = grade;
    }

    /**
     * Parses a single line into a {@link StudentRecord}.
     *
     * @param line the line to parse
     * @return the parsed {@link StudentRecord}
     *
     * @throws IllegalArgumentException if the line does not represent a valid {@link StudentRecord}
     */
    public static StudentRecord parse(String line) {
        String[] parts = line.split("\\t");

        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid number of arguments in line, expected 7, got " + parts.length);
        }

        return new StudentRecord(
                parts[0],
                parts[1],
                parts[2],
                parseDoubleOrThrowILE(parts[3]),
                parseDoubleOrThrowILE(parts[4]),
                parseDoubleOrThrowILE(parts[5]),
                parseIntOrThrowILE(parts[6])
        );
    }

    /**
     * Parses the string argument as a double. If parsing method throws an {@link NumberFormatException},
     * it throws a new {@link IllegalArgumentException} with a relevant message.
     *
     * @param toParse the string to parse as a double
     * @return the parsed double
     *
     * @throws IllegalArgumentException if the argument does not represent a valid double
     */
    private static double parseDoubleOrThrowILE(String toParse) {
        try {
            return Double.parseDouble(toParse);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + toParse + "' is not a valid double.");
        }
    }

    /**
     * Parses the string argument as an integer. If parsing method throws an {@link NumberFormatException},
     * it throws a new {@link IllegalArgumentException} with a relevant message.
     *
     * @param toParse the string to parse as an integer
     * @return the parsed integer
     *
     * @throws IllegalArgumentException if the argument does not represent a valid integer
     */
    private static int parseIntOrThrowILE(String toParse) {
        try {
            return Integer.parseInt(toParse);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + toParse + "' is not a valid integer.");
        }
    }

    /**
     * Returns the student's JMBAG.
     *
     * @return the student's JMBAG
     */
    public String getJmbag() {
        return jmbag;
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
     * Returns the student's first name.
     *
     * @return the student's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the number of points the student has earned on the midterm.
     *
     * @return the number of points the student has earned on the midterm
     */
    public double getPointsOnMidterm() {
        return pointsOnMidterm;
    }

    /**
     * Returns the number of points the student has earned on the final.
     *
     * @return the number of points the student has earned on the final
     */
    public double getPointsOnFinal() {
        return pointsOnFinal;
    }

    /**
     * Returns the number of points the student has earned from the exercises.
     *
     * @return the number of points the student has earned from the exercises
     */
    public double getPointsFromExercises() {
        return pointsFromExercises;
    }

    /**
     * Returns the student's final grade in the class.
     *
     * @return the student's final grade in the class
     */
    public int getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return jmbag + "\t"
             + lastName + "\t"
             + firstName + "\t"
             + pointsOnMidterm + "\t"
             + pointsOnFinal + "\t"
             + pointsFromExercises + "\t"
             + grade;
    }

}
