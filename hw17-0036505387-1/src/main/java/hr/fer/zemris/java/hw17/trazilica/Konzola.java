package hr.fer.zemris.java.hw17.trazilica;

import hr.fer.zemris.java.hw17.trazilica.model.DocumentGroup;
import hr.fer.zemris.java.hw17.trazilica.model.DocumentVector;
import hr.fer.zemris.java.hw17.trazilica.model.document.Document;
import hr.fer.zemris.java.hw17.trazilica.model.document.FileDocument;
import hr.fer.zemris.java.hw17.trazilica.model.document.StringDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Konzola {

    private static DocumentGroup documentGroup;

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
            System.out.print("Enter command > ");

            String line = sc.nextLine().trim();
            String[] parts = line.split(" ");

            switch (parts[0]) {
                case "exit":
                    return;
                case "query":
                    query(parts);
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }

    private static void query(String[] parts) {
        String[] query = Arrays.copyOfRange(parts, 1, parts.length);

        System.out.println("Query is: " + Arrays.toString(query));

        Document queryDocument = new StringDocument(String.join(" ", query));
        System.out.println(queryDocument.getWordFrequency());
        DocumentVector documentVector = queryDocument.toDocumentVector(documentGroup);


        for (DocumentVector documentVector1 : documentGroup.getDocumentVectors()) {
            System.out.println(documentVector.similarity(documentVector1));
        }
    }

}
