package hr.fer.zemris.java.hw17.trazilica;

import hr.fer.zemris.java.hw17.trazilica.model.DocumentGroup;
import hr.fer.zemris.java.hw17.trazilica.model.DocumentVector;
import hr.fer.zemris.java.hw17.trazilica.model.document.Document;
import hr.fer.zemris.java.hw17.trazilica.model.document.FileDocument;
import hr.fer.zemris.java.hw17.trazilica.model.document.StringDocument;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Konzola {

    private static DocumentGroup documentGroup;
    private static List<Result> results;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong number of arguments: 1 argument expected, the path to the directory of documents.");
            System.exit(1);
        }

        Path documentsDir = Paths.get(args[0]);

        if (!Files.isDirectory(documentsDir)) {
            System.out.println("'" + args[0] + "' is not a readable directory.");
            System.exit(1);
        }

        List<FileDocument> documents = null;

        try {
            documents = Files.list(documentsDir)
                             .map(FileDocument::new)
                             .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error occurred while reading from '" + args[0] + "'.");
            System.exit(1);
        }

        documentGroup = new DocumentGroup(documents);

        promptForCommand();
    }

    private static void promptForCommand() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Vocabulary has " + documentGroup.getVocabulary().size() + " words.");

        while (true) {
            System.out.println();
            System.out.print("Enter command > ");

            String line = sc.nextLine().trim();
            String[] parts = line.split(" ");

            switch (parts[0]) {
                case "exit":
                    return;
                case "query":
                    query(parts);
                    break;
                case "type":
                    type(parts);
                    break;
                case "results":
                    results();
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }

    private static void results() {
        // We can't use an int in a lambda to keep track of the index and Integer is immutable
        AtomicInteger index = new AtomicInteger();

        results.stream()
                .limit(10)
                .forEach(r -> System.out.format("[%2d] %s%n", index.getAndIncrement(), r.toString()));
    }

    private static void type(String[] parts) {
        if (results == null) {
            System.out.println("You haven't queried anything yet!");
            return;
        }

        if (parts.length != 2) {
            System.out.println("Type command expects exactly one parameter, the index of the result to print.");
            return;
        }

        int index;

        try {
            index = Integer.parseInt(parts[1]);
        }
        catch (NumberFormatException e) {
            System.out.println("'" + parts[1] + "' is not a valid integer.");
            return;
        }

        if (index < 0 || index >= results.size()) {
            System.out.println("Index is out of range.");
            return;
        }

        Path documentToPrint = results.get(index).document.getPathToDocument();

        System.out.println("Document: " + documentToPrint.toAbsolutePath().toString());
        System.out.println("-".repeat(30));

        try {
            Files.lines(documentToPrint, StandardCharsets.UTF_8).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error occurred while reading file '" + documentToPrint.toAbsolutePath().toString() + "'.");
        }
    }

    private static void query(String[] parts) {
        String[] query = Arrays.copyOfRange(parts, 1, parts.length);

        System.out.println("Query is: " + Arrays.toString(query));

        Document queryDocument = new StringDocument(String.join(" ", query));
        DocumentVector documentVector = queryDocument.toDocumentVector(documentGroup);

        results = documentGroup.getDocumentVectors()
                .stream()
                .map(dv -> new Result(dv, documentVector.similarity(dv)))
                .filter(r -> r.similarity > 1E-6)
                .sorted()
                .collect(Collectors.toList());

        System.out.println("10 best results: ");
        results();
    }

    private static class Result implements Comparable<Result> {

        private final DocumentVector document;
        private final double similarity;

        private Result(DocumentVector document, double similarity) {
            this.document = document;
            this.similarity = similarity;
        }

        @Override
        public int compareTo(Result o) {
            return -Double.compare(this.similarity, o.similarity);
        }

        @Override
        public String toString() {
            return String.format("(%.4f) %s", similarity, document.getPathToDocument().toAbsolutePath().toString());
        }

    }

}
