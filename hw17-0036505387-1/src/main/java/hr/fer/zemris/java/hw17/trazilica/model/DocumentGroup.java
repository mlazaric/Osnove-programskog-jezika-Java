package hr.fer.zemris.java.hw17.trazilica.model;

import hr.fer.zemris.java.hw17.trazilica.model.document.FileDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.log;

/**
 * Models a group of {@link hr.fer.zemris.java.hw17.trazilica.model.document.Document}s which can be searched together
 * using an IDF vector.
 *
 * @author Marko Lazarić
 */
public class DocumentGroup {

    /**
     * The path to the file containing the stop words for that language.
     */
    private static final String PATH_TO_STOP_WORDS = "src/main/resources/stopwords.txt";

    /**
     * The list of words used in the documents without stop words.
     *
     * Notice: it is a {@link List} since we need to guarantee an ordering, but it could have also been a {@link Set}.
     */
    private final List<String> vocabulary;

    /**
     * The generated IDF vector.
     */
    private DocumentVector idfVector;

    /**
     * The list of {@link DocumentVector}s contained in this {@link DocumentGroup}.
     */
    private List<DocumentVector> documentVectors;

    /**
     * Creates a new {@link DocumentGroup} with the given argument.
     *
     * @param documents the list of {@link hr.fer.zemris.java.hw17.trazilica.model.document.Document} contained in this {@link DocumentGroup}
     */
    public DocumentGroup(List<FileDocument> documents) {
        this.vocabulary = new ArrayList<>();

        buildVocabulary(documents);
        buildWordFrequencies(documents);
        buildNumberOfDocumentsForWord(documents);
        buildDocumentVectors(documents);
    }

    /**
     * Builds the list of all words from all contained documents and removes the stop words.
     */
    private void buildVocabulary(List<FileDocument> documents) {
        Set<String> wordSet = new HashSet<>();

        // Add vocabularies from all documents
        documents.stream()
                 .map(FileDocument::getVocabulary)
                 .forEach(wordSet::addAll);

        // Remove the stop words from the vocabulary
        wordSet.removeAll(getStopWords());

        vocabulary.addAll(wordSet);
    }

    /**
     * Builds word frequencies in all contained documents.
     */
    private void buildWordFrequencies(List<FileDocument> documents) {
        documents.forEach(d -> d.buildWordFrequency(vocabulary));
    }

    /**
     * For each word in the vocabulary, it calculates the number of documents that contain that word.
     */
    private void buildNumberOfDocumentsForWord(List<FileDocument> documents) {
        Map<String, Integer> numberOfDocumentsWithWord = new HashMap<>();

        documents.stream()
                 .map(FileDocument::getWordFrequency)
                 .map(Map::keySet)
                 .forEach(words -> {
                     for (String word : words) {
                         numberOfDocumentsWithWord.merge(word, 1, Integer::sum);
                     }
                 });

        buildIDFVector(documents, numberOfDocumentsWithWord);
    }

    /**
     * Returns all the stop words loaded from the file.
     *
     * @return all the stop words loaded from the file
     */
    private Set<String> getStopWords() {
        try {
            return Files.lines(Paths.get(PATH_TO_STOP_WORDS))
                        .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading stop words from " + PATH_TO_STOP_WORDS, e);
        }
    }

    /**
     * Creates the IDF vector.
     */
    private void buildIDFVector(List<FileDocument> documents, Map<String, Integer> numberOfDocumentsWithWord) {
        double[] vector = new double[vocabulary.size()];
        int index = 0;

        for (String word : vocabulary) {
            vector[index] = log((double) documents.size() / numberOfDocumentsWithWord.get(word)); // IDF component

            index++;
        }

        idfVector =  new DocumentVector(vector);
    }

    /**
     * For each {@link hr.fer.zemris.java.hw17.trazilica.model.document.Document}, it creates a {@link DocumentVector}
     * which is easier to search with.
     */
    private void buildDocumentVectors(List<FileDocument> documents) {
        documentVectors = documents.stream()
                                   .map(doc -> doc.toDocumentVector(this))
                                   .collect(Collectors.toList());
    }


    /**
     * Returns the list of words used in the documents without stop words.
     *
     * @return the list of words used in the documents without stop words
     */
    public List<String> getVocabulary() {
        return vocabulary;
    }

    /**
     * Returns the generated IDF vector.
     *
     * @return the generated IDF vector
     */
    public DocumentVector getIdfVector() {
        return idfVector;
    }

    /**
     * Returns the list of {@link DocumentVector}s contained in this {@link DocumentGroup}..
     *
     * @return the list of {@link DocumentVector}s contained in this {@link DocumentGroup}.
     */
    public List<DocumentVector> getDocumentVectors() {
        return documentVectors;
    }

}
