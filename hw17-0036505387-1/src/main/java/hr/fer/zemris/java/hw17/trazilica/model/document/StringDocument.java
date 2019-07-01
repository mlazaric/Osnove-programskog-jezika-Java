package hr.fer.zemris.java.hw17.trazilica.model.document;

import hr.fer.zemris.java.hw17.trazilica.model.DocumentGroup;
import hr.fer.zemris.java.hw17.trazilica.model.DocumentVector;
import hr.fer.zemris.java.hw17.trazilica.model.LexerUtil;

import java.util.*;

public class StringDocument implements Document {

    private final String contents;
    private final Map<String, Integer> wordFrequency;

    public StringDocument(String contents) {
        this.contents = Objects.requireNonNull(contents, "Contents cannot be null.");
        this.wordFrequency = new HashMap<>();

        buildWordFrequency();
    }

    public void buildWordFrequency() {
        List<String> words = LexerUtil.extractWords(contents);

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
