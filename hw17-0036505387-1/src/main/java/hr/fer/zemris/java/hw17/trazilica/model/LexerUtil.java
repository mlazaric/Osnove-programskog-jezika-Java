package hr.fer.zemris.java.hw17.trazilica.model;

import java.util.ArrayList;
import java.util.List;

public class LexerUtil {

    public static List<String> extractWords(String line) {
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
