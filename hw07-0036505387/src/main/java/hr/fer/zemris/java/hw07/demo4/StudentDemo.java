package hr.fer.zemris.java.hw07.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simple program for manipulating student records and demonstrating the usage of {@link java.util.stream.Stream}s.
 *
 * @author Marko Lazarić
 */
public class StudentDemo {

    /**
     * Reads './studenti.txt', parses the student records, preforms the manipulations and prints the results.
     * If an error occurs, a relevant error message is printed to the user.
     *
     * @param args the arguments are ignored
     */
    public static void main(String[] args) {
        List<String> lines = null;

        try {
            lines = Files.readAllLines(Paths.get("./studenti.txt"));
        } catch (IOException e) {
            System.err.println("'./studenti.txt' does not exist.");
            System.exit(1);
        }

        List<StudentRecord> records = null;

        try {
            records = convert(lines);
        } catch (IllegalArgumentException e) {
            System.err.println("Error occurred while parsing './studenti.txt'.");
            System.exit(1);
        }

        System.out.println("Zadatak 1");
        System.out.println("=========");
        System.out.println(vratiBodovaViseOd25(records));

        System.out.println("Zadatak 2");
        System.out.println("=========");
        System.out.println(vratiBrojOdlikasa(records));

        System.out.println("Zadatak 3");
        System.out.println("=========");
        vratiListuOdlikasa(records).stream()
                                   .forEach(System.out::println);

        System.out.println("Zadatak 4");
        System.out.println("=========");
        vratiSortiranuListuOdlikasa(records).stream()
                                            .forEach(System.out::println);

        System.out.println("Zadatak 5");
        System.out.println("=========");
        vratiPopisNepolozenih(records).stream()
                                      .forEach(System.out::println);

        System.out.println("Zadatak 6");
        System.out.println("=========");
        razvrstajStudentePoOcjenama(records).forEach((grade, list) -> {
            System.out.println(grade);

            list.stream()
                .forEach(System.out::println);
        });

        System.out.println("Zadatak 7");
        System.out.println("=========");
        vratiBrojStudenataPoOcjenama(records).forEach((grade, number) -> System.out.println(grade + ": " + number));

        System.out.println("Zadatak 8");
        System.out.println("=========");
        razvrstajProlazPad(records).forEach((passed, list) -> {
            System.out.println(passed);

            list.stream()
                .forEach(System.out::println);
        });
    }

    /**
     * Sums the points the student has earned in the class and returns a grand total.
     *
     * @param record the record representing a student in the class
     * @return the total number of points earned by that student
     */
    private static double getTotalPoints(StudentRecord record) {
        return record.getPointsOnMidterm() +
                record.getPointsOnFinal() +
                record.getPointsFromExercises();
        // This could go into StudentRecord, but let's pretend StudentRecords is a part of an API
        // that we can't change.

        // We could inline this but leaving it as a function simplifies some of the lambdas, for
        // example the comparator in vratiSortiranuListuOdlikasa.
    }

    /**
     * Converts a list of strings where each line represents a single student record, to a list
     * of {@link StudentRecord}s.
     *
     * @param lines the list of strings to parse
     * @return a list of the parsed records
     */
    private static List<StudentRecord> convert(List<String> lines) {
        return lines.stream()
                    .filter(line -> !line.isBlank())
                    .map(StudentRecord::parse)
                    .collect(Collectors.toList());
    }

    /**
     * Returns the number of students that have earned more than 25 points.
     *
     * @param records the records representing students enrolled in the class
     * @return the number of students that have earned more than 25 points
     */
    private static long vratiBodovaViseOd25(List<StudentRecord> records) {
        return records.stream()
                      .filter(r -> getTotalPoints(r) > 25)
                      .count();
    }

    /**
     * Returns the number of students that have received the highest grade (5).
     *
     * @param records the records representing students enrolled in the class
     * @return the number of students that have received the highest grade
     */
    private static long vratiBrojOdlikasa(List<StudentRecord> records) {
        return records.stream()
                      .filter(r -> r.getGrade() == 5)
                      .count();
    }

    /**
     * Returns a list of students that have received the highest grade (5).
     *
     * @param records the records representing students enrolled in the class
     * @return a list of students that have received the highest grade
     */
    private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
        return records.stream()
                      .filter(r -> r.getGrade() == 5)
                      .collect(Collectors.toList());
    }

    /**
     * Returns an ordered list of students that have received the highest grade (5). It is sorted descending based
     * on the number of points earned.
     *
     * @param records the records representing students enrolled in the class
     * @return an ordered list of students that have received the highest grade
     */
    private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
        return records.stream()
                      .filter(r -> r.getGrade() == 5)
                      .sorted(Comparator.comparingDouble(StudentDemo::getTotalPoints).reversed())
                      .collect(Collectors.toList());
    }

    /**
     * Returns a list of students that have not passed the class.
     *
     * @param records the records representing students enrolled in the class
     * @return students that have not passed the class
     */
    private static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
        return records.stream()
                      .filter(r -> r.getGrade() == 1)
                      .map(StudentRecord::getJmbag)
                      .sorted() // String implements Comparable.
                      .collect(Collectors.toList());
    }

    /**
     * Groups students based on their final grade.
     *
     * @param records the records representing students enrolled in the class
     * @return students grouped by their final grade
     *
     */
    private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
        return records.stream()
                      .collect(Collectors.groupingBy(StudentRecord::getGrade));
    }

    /**
     * For each grade, it calculates the number of students that have received that grade.
     *
     * @param records the records representing students enrolled in the class
     * @return for each grade, the number of students to have received that grade
     */
    private static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
        return records.stream()
                      .collect(Collectors.toMap(StudentRecord::getGrade, r -> 1, Integer::sum));
    }

    /**
     * Partitions students into two partitions, those who have passed the class (whose grade is > 1) and those
     * who have not (whose grade is 1).
     *
     * @param records the records representing students enrolled in the class
     * @return students partitioned by whether they have passed the class
     */
    private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
        return records.stream()
                      .collect(Collectors.partitioningBy(r -> r.getGrade() > 1));
    }


}
