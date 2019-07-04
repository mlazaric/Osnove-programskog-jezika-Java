package hr.fer.zemris.java.hw17.trazilica.model.document;

import hr.fer.zemris.java.hw17.trazilica.model.DocumentGroup;
import hr.fer.zemris.java.hw17.trazilica.model.DocumentVector;

import java.util.*;

/**
 * A concrete implementation of a {@link Document} which uses a {@link String} as the document.
 *
 * @author Marko Lazarić
 */
public class StringDocument implements Document {

    /**
     * Contents of the {@link String} document.
     */
    private final String contents;

    /**
     * The mapping of words to their frequencies.
     */
    private final Map<String, Integer> wordFrequency;

    /**
     * Creates a new {@link StringDocument} with the given argument.
     *
     * @param contents the contents of the {@link String} document
     */
    public StringDocument(String contents) {
        this.contents = Objects.requireNonNull(contents, "Contents cannot be null.");
        this.wordFrequency = new HashMap<>();

        buildWordFrequency();
    }

    /**
     * Builds the word frequencies table.
     */
    public void buildWordFrequency() {
        List<String> words = extractWords(contents);

        for (String word : words) {
            wordFrequency.merge(word, 1, Integer::sum);
        }
    }

    @Override
    public Set<String> getVocabulary() {
        return wordFrequency.keySet();
    }

    @Override
    public Map<String, Integer> getWordFrequency() {
        return wordFrequency;
    }

    @Override
    public DocumentVector toDocumentVector(DocumentGroup documentGroup) {
        return toDocumentVector(null, documentGroup);
    }
}
