package hr.fer.zemris.java.hw07.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentDemo {

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

        printTaskHeader(1);
        System.out.println(vratiBodovaViseOd25(records));

        printTaskHeader(2);
        System.out.println(vratiBrojOdlikasa(records));

        printTaskHeader(3);
        vratiListuOdlikasa(records).stream()
                                   .forEach(System.out::println);

        printTaskHeader(4);
        vratiSortiranuListuOdlikasa(records).stream()
                                            .forEach(System.out::println);

        printTaskHeader(5);
        vratiPopisNepolozenih(records).stream()
                                      .forEach(System.out::println);

        printTaskHeader(6);
        razvrstajStudentePoOcjenama(records).forEach((grade, list) -> {
            System.out.println(grade);

            list.stream()
                .forEach(System.out::println);
        });

        printTaskHeader(7);
        vratiBrojStudenataPoOcjenama(records).forEach((grade, number) -> System.out.println(grade + ": " + number));

        printTaskHeader(8);
        razvrstajProlazPad(records).forEach((passed, list) -> {
            System.out.println(passed);

            list.stream()
                .forEach(System.out::println);
        });
    }

    private static void printTaskHeader(int taskNumber) {
        System.out.println("Zadatak " + taskNumber);
        System.out.println("=========");
    }

    private static double getTotalPoints(StudentRecord record) {
        return record.getPointsOnMidterm() +
                record.getPointsOnFinal() +
                record.getPointsFromExercises();
        // This could go into StudentRecord, but let's pretend StudentRecords is a part of an API
        // that we can't change.

        // We could inline this but leaving it as a function simplifies some of the lambdas, for
        // example the comparator in vratiSortiranuListuOdlikasa.
    }

    private static List<StudentRecord> convert(List<String> lines) {
        return lines.stream()
                    .filter(line -> !line.isBlank())
                    .map(StudentRecord::parse)
                    .collect(Collectors.toList());
    }

    private static long vratiBodovaViseOd25(List<StudentRecord> records) {
        return records.stream()
                      .filter(r -> getTotalPoints(r) > 25)
                      .count();
    }

    private static long vratiBrojOdlikasa(List<StudentRecord> records) {
        return records.stream()
                      .filter(r -> r.getGrade() == 5)
                      .count();
    }

    private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
        return records.stream()
                      .filter(r -> r.getGrade() == 5)
                      .collect(Collectors.toList());
    }

    private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
        return records.stream()
                      .filter(r -> r.getGrade() == 5)
                      .sorted(Comparator.comparingDouble(StudentDemo::getTotalPoints).reversed())
                      .collect(Collectors.toList());
    }

    private static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
        return records.stream()
                      .filter(r -> r.getGrade() == 1)
                      .map(StudentRecord::getJmbag)
                      .sorted() // String implements Comparable.
                      .collect(Collectors.toList());
    }

    private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
        return records.stream()
                      .collect(Collectors.groupingBy(StudentRecord::getGrade));
    }

    private static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
        return records.stream()
                      .collect(Collectors.toMap(StudentRecord::getGrade, r -> 1, Integer::sum));
    }

    private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
        return records.stream()
                      .collect(Collectors.partitioningBy(r -> r.getGrade() > 1));
    }


}
