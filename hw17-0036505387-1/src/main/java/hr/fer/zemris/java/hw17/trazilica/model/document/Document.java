package hr.fer.zemris.java.hw17.trazilica.model.document;

import hr.fer.zemris.java.hw17.trazilica.model.DocumentGroup;
import hr.fer.zemris.java.hw17.trazilica.model.DocumentVector;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Models an abstract document which is represented by its vocabulary and its word frequency.
 * It should also implement creating a {@link DocumentVector} from itself.
 *
 * @author Marko Lazarić
 */
public interface Document {

    /**
     * Returns the vocabulary of the document.
     *
     * Note: it may include stop words.
     *
     * @return a set containing all the words in the document
     */
    Set<String> getVocabulary();

    /**
     * Returns a map of word frequencies.
     *
     * Note: it may include stop words.
     *
     * @return a mapping of words to their frequencies
     */
    Map<String, Integer> getWordFrequency();

    /**
     * Creates a {@link DocumentVector} from itself using the given {@link DocumentGroup}.
     *
     * @param documentGroup the group of documents to use for additional information
     * @return the newly created {@link DocumentVector}
     */
    DocumentVector toDocumentVector(DocumentGroup documentGroup);

    /**
     * Creates a {@link DocumentVector} from itself using the given {@link DocumentGroup} and sets its path to the given
     * argument.
     *
     * @param pathToFile the path to the file represented by this {@link Document}
     * @param documentGroup the group of documents to use for additional information
     * @return the newly created {@link DocumentVector}
     */
    default DocumentVector toDocumentVector(Path pathToFile, DocumentGroup documentGroup) {
        List<String> vocabulary = documentGroup.getVocabulary();
        DocumentVector idfVector = documentGroup.getIdfVector();

        double[] vector = new double[vocabulary.size()];
        int index = 0;

        for (String word : vocabulary) {
            vector[index] = getWordFrequency().getOrDefault(word, 0) * // TF component
                    idfVector.getVector()[index]; // IDF component

            index++;
        }

        return new DocumentVector(vector, pathToFile);
    }

    /**
     * Extracts a list of words from a line of text.
     *
     * @param line the line of text to extract from
     * @return a list of the extracted words
     */
    default List<String> extractWords(String line) {
        List<String> words = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (char character : line.toCharArray()) {
            if (Character.isAlphabetic(character)) {
                sb.append(character);
            }
            else if (sb.length() > 0) {
                words.add(sb.toString().toLowerCase());
                sb.setLength(0);
            }
        }

        // Add last word
        if (sb.length() > 0) {
            words.add(sb.toString().toLowerCase());
        }

        return words;
    }

}
