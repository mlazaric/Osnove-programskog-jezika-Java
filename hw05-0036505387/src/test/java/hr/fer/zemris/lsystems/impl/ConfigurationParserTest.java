package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

class ConfigurationParserTest {

	static String[] readLines(String path) {
		List<String> lines;

		try {
			lines = Files.readAllLines(
					Paths.get(path),
					StandardCharsets.UTF_8
					);
		} catch (IOException e) {
			fail(e.getMessage());

			return null; // this is just so eclipse doesn't complain
		}

		return lines.toArray(new String[lines.size()]);
	}

	@Test
	void testParsingHilbertCurve() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();

		builder.configureFromText(readLines("src/test/resources/examples/hilbertCurve.txt"));
	}

	@Test
	void testParsingKoch2() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();

		builder.configureFromText(readLines("src/test/resources/examples/koch2.txt"));
	}

	@Test
	void testParsingKochCurve() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();

		builder.configureFromText(readLines("src/test/resources/examples/kochCurve.txt"));
	}

	@Test
	void testParsingKochIsland() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();

		builder.configureFromText(readLines("src/test/resources/examples/kochIsland.txt"));
	}

	@Test
	void testParsingPlant1() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();

		builder.configureFromText(readLines("src/test/resources/examples/plant1.txt"));
	}

	@Test
	void testParsingPlant2() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();

		builder.configureFromText(readLines("src/test/resources/examples/plant2.txt"));
	}

	@Test
	void testParsingPlant3() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();

		builder.configureFromText(readLines("src/test/resources/examples/plant3.txt"));
	}

	@Test
	void testParsingSierpinskiGasket() {
		LSystemBuilderImpl builder = new LSystemBuilderImpl();

		builder.configureFromText(readLines("src/test/resources/examples/sierpinskiGasket.txt"));
	}

}
