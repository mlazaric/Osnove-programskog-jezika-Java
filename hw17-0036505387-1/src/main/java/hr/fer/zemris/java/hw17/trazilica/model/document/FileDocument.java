package hr.fer.zemris.java.hw17.trazilica.model.document;

import hr.fer.zemris.java.hw17.trazilica.model.DocumentGroup;
import hr.fer.zemris.java.hw17.trazilica.model.DocumentVector;
import hr.fer.zemris.java.hw17.trazilica.model.LexerUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileDocument implements Document {

    private final Path pathToDocument;
    private Map<String, Integer> wordFrequency;

    public FileDocument(Path pathToDocument) {
        this.pathToDocument = pathToDocument;
        this.wordFrequency = new HashMap<>();
    }

    @Override
    public Set<String> getVocabulary() {
        Set<String> vocabulary = new HashSet<>();

        try {
            Files.lines(pathToDocument, StandardCharsets.UTF_8)
                 .map(LexerUtil::extractWords)
                 .forEach(vocabulary::addAll);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while building vocabulary.", e);
        }

        return vocabulary;
    }

    public void buildWordFrequency(List<String> listOfWords) {
        Set<String> vocabulary = new HashSet<>(listOfWords);

        try {
            Files.lines(pathToDocument, StandardCharsets.UTF_8)
                 .map(LexerUtil::extractWords)
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
