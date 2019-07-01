package hr.fer.zemris.java.hw17.trazilica;

import hr.fer.zemris.java.hw17.trazilica.model.Document;
import hr.fer.zemris.java.hw17.trazilica.model.DocumentGroup;
import hr.fer.zemris.java.hw17.trazilica.model.DocumentVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

        List<Document> documents = null;

        try {
            documents = Files.list(documentsDir)
                             .map(Document::new)
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

        Document document = new Document(null);
        Map<String, Integer> wordFrequency = document.getWordFrequency();
        Set<String> wordSet = new HashSet<>(documentGroup.getVocabulary());

        for (String word : query) {
            if (wordSet.contains(word)) {
                wordFrequency.merge(word, 1, Integer::sum);
            }
        }
        System.out.println(wordFrequency);

        DocumentVector documentVector = documentGroup.buildDocumentVector(document);

        for (DocumentVector documentVector1 : documentGroup.getDocumentVectors()) {
            System.out.println(documentVector.similarity(documentVector1));
        }
    }

}
