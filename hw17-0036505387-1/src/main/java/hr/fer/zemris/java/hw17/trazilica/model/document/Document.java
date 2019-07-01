package hr.fer.zemris.java.hw17.trazilica.model.document;

import hr.fer.zemris.java.hw17.trazilica.model.DocumentGroup;
import hr.fer.zemris.java.hw17.trazilica.model.DocumentVector;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Document {

    Set<String> getVocabulary();
    Map<String, Integer> getWordFrequency();

    DocumentVector toDocumentVector(DocumentGroup documentGroup);

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

}
