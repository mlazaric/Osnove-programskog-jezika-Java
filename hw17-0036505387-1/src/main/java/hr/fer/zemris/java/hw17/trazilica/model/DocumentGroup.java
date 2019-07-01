package hr.fer.zemris.java.hw17.trazilica.model;

import hr.fer.zemris.java.hw17.trazilica.model.document.FileDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.log;

public class DocumentGroup {

    private static final String PATH_TO_STOP_WORDS = "src/main/resources/stopwords.txt";

    private final List<String> vocabulary;

    private DocumentVector idfVector;
    private List<DocumentVector> documentVectors;

    public DocumentGroup(List<FileDocument> documents) {
        this.vocabulary = new ArrayList<>();

        buildVocabulary(documents);
        buildWordFrequencies(documents);
        buildNumberOfDocumentsForWord(documents);
        buildDocumentVectors(documents);
    }

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

    private void buildWordFrequencies(List<FileDocument> documents) {
        documents.forEach(d -> d.buildWordFrequency(vocabulary));
    }

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

    private Set<String> getStopWords() {
        try {
            return Files.lines(Paths.get(PATH_TO_STOP_WORDS))
                        .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading stop words from " + PATH_TO_STOP_WORDS, e);
        }
    }

    private void buildIDFVector(List<FileDocument> documents, Map<String, Integer> numberOfDocumentsWithWord) {
        double[] vector = new double[vocabulary.size()];
        int index = 0;

        for (String word : vocabulary) {
            vector[index] = log((double) documents.size() / numberOfDocumentsWithWord.get(word)); // IDF component

            index++;
        }

        idfVector =  new DocumentVector(vector);
    }

    private void buildDocumentVectors(List<FileDocument> documents) {
        documentVectors = documents.stream()
                                   .map(doc -> doc.toDocumentVector(this))
                                   .collect(Collectors.toList());
    }



    public List<String> getVocabulary() {
        return vocabulary;
    }

    public DocumentVector getIdfVector() {
        return idfVector;
    }

    public List<DocumentVector> getDocumentVectors() {
        return documentVectors;
    }
}
