package hr.fer.zemris.java.hw17.trazilica.model.document;

import hr.fer.zemris.java.hw17.trazilica.model.DocumentGroup;
import hr.fer.zemris.java.hw17.trazilica.model.DocumentVector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * A concrete implementation of a {@link Document} which uses a text file as the document.
 *
 * @author Marko Lazarić
 */
public class FileDocument implements Document {

    /**
     * The path to the file whose document this is.
     */
    private final Path pathToDocument;

    /**
     * The mapping of words to their frequencies.
     */
    private Map<String, Integer> wordFrequency;

    /**
     * Creates a new {@link FileDocument} with the given arguments.
     *
     * @param pathToDocument the path to the file whose document this is
     */
    public FileDocument(Path pathToDocument) {
        this.pathToDocument = pathToDocument;
        this.wordFrequency = new HashMap<>();
    }

    @Override
    public Set<String> getVocabulary() {
        Set<String> vocabulary = new HashSet<>();

        try {
            Files.lines(pathToDocument, StandardCharsets.UTF_8)
                 .map(this::extractWords)
                 .forEach(vocabulary::addAll);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while building vocabulary.", e);
        }

        return vocabulary;
    }

    /**
     * Builds the word frequencies table.
     *
     * @param listOfWords the vocabulary of the {@link DocumentGroup}
     */
    public void buildWordFrequency(List<String> listOfWords) {
        Set<String> vocabulary = new HashSet<>(listOfWords);

        try {
            Files.lines(pathToDocument, StandardCharsets.UTF_8)
                 .map(this::extractWords)
                 .forEach(words -> {
                     for (String word : words) {
                         if (vocabulary.contains(word)) {
                             wordFrequency.merge(word, 1, Integer::sum);
                         }
                     }
                 });
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while building word frequency.", e);
        }
    }

    /**
     * Returns the path to the file whose document this is.
     *
     * @return the path to the file whose document this is
     */
    public Path getPathToDocument() {
        return pathToDocument;
    }

    @Override
    public Map<String, Integer> getWordFrequency() {
        return wordFrequency;
    }

    @Override
    public DocumentVector toDocumentVector(DocumentGroup documentGroup) {
        return toDocumentVector(pathToDocument, documentGroup);
    }

}
