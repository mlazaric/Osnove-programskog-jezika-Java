package hr.fer.zemris.java.hw07.demo4;

import java.util.Objects;

public class StudentRecord {

    private final String jmbag;
    private final String lastName;
    private final String firstName;
    private final double pointsOnMidterm;
    private final double pointsOnFinal;
    private final double pointsFromExercises;
    private final int grade;

    public StudentRecord(String jmbag, String lastName, String firstName, double pointsOnMidterm, double pointsOnFinal, double pointsFromExercises, int grade) {
        this.jmbag = Objects.requireNonNull(jmbag, "JMBAG cannot be null.");
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null.");
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null.");
        this.pointsOnMidterm = pointsOnMidterm;
        this.pointsOnFinal = pointsOnFinal;
        this.pointsFromExercises = pointsFromExercises;
        this.grade = grade;
    }

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

    private static double parseDoubleOrThrowILE(String toParse) {
        try {
            return Double.parseDouble(toParse);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + toParse + "' is not a valid double.");
        }
    }

    private static int parseIntOrThrowILE(String toParse) {
        try {
            return Integer.parseInt(toParse);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + toParse + "' is not a valid integer.");
        }
    }

    public String getJmbag() {
        return jmbag;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public double getPointsOnMidterm() {
        return pointsOnMidterm;
    }

    public double getPointsOnFinal() {
        return pointsOnFinal;
    }

    public double getPointsFromExercises() {
        return pointsFromExercises;
    }

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
