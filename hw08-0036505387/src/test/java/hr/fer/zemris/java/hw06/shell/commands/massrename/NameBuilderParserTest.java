package hr.fer.zemris.java.hw06.shell.commands.massrename;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static hr.fer.zemris.java.hw06.shell.commands.massrename.NameBuilderParser.group;
import static hr.fer.zemris.java.hw06.shell.commands.massrename.NameBuilderParser.text;
import static org.junit.jupiter.api.Assertions.*;

class NameBuilderParserTest {

    @Test
    void testParsingValidExpressions() {
        assertDoesNotThrow(() -> new NameBuilderParser("${1,03};"));
        assertDoesNotThrow(() -> new NameBuilderParser("${ 1 , 03 };"));
        assertDoesNotThrow(() -> new NameBuilderParser("${1 , 03};"));
        assertDoesNotThrow(() -> new NameBuilderParser("${ 1 };  "));
    }

    @Test
    void testParsingInvalidExpressions() {
        assertThrows(NullPointerException.class, () ->  new NameBuilderParser(null));

        assertThrows(IllegalArgumentException.class, () -> new NameBuilderParser("${}"));
        assertThrows(IllegalArgumentException.class, () -> new NameBuilderParser("${a}"));
        assertThrows(IllegalArgumentException.class, () -> new NameBuilderParser("${a,b}"));
        assertThrows(IllegalArgumentException.class, () -> new NameBuilderParser("${1,bc}"));
        assertThrows(IllegalArgumentException.class, () -> new NameBuilderParser("${,}"));
        assertThrows(IllegalArgumentException.class, () -> new NameBuilderParser("${,,,}"));
    }

    @Test
    void testExampleFromPDF() {
        // massrename DIR1 DIR2 show slika(\d+)-([^.]+)\.jpg gradovi-${2}-${1,03}.jpg
        Pattern pattern = Pattern.compile("slika(\\d+)-([^.]+)\\.jpg", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        NameBuilderParser nameBuilderParser = new NameBuilderParser("gradovi-${2}-${1,03}.jpg");

        List<String> input = List.of(
            "slika1-zagreb.jpg",
            "slika2-zagreb.jpg",
            "slika3-zagreb.jpg",
            "slika4-zagreb.jpg",
            "slika1-zadar.jpg",
            "slika2-zadar.jpg",
            "slika3-zadar.jpg",
            "slika4-zadar.jpg"
        );

        List<String> expectedOutput = List.of(
            "gradovi-zagreb-001.jpg",
            "gradovi-zagreb-002.jpg",
            "gradovi-zagreb-003.jpg",
            "gradovi-zagreb-004.jpg",
            "gradovi-zadar-001.jpg",
            "gradovi-zadar-002.jpg",
            "gradovi-zadar-003.jpg",
            "gradovi-zadar-004.jpg"
        );

        List<String> actualOutput = input.stream()
                                         .map(pattern::matcher)
                                         .filter(Matcher::matches)
                                         .map(FilterResult::new)
                                         .map(fr -> {
                                            StringBuilder sb = new StringBuilder();

                                            nameBuilderParser.getNameBuilder().execute(fr, sb);

                                            return sb.toString();
                                         }).collect(Collectors.toList());

        assertEquals(expectedOutput, actualOutput);
    }
}