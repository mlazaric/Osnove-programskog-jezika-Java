package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptTester {

	public static void main(String[] args) throws IOException {
		String docBody = new String(
				Files.readAllBytes(Paths.get("src/test/resources/document40.txt")),
				StandardCharsets.UTF_8);

		SmartScriptParser parser = null;

		try {
			parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
			System.out.println(e.getMessage());
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch(Exception e) {
			e.printStackTrace();

			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}

		DocumentNode document = parser.getDocumentNode();

		String originalDocumentBody = createOriginalDocumentBody(document);

		System.out.println(originalDocumentBody);
	}

	private static String createOriginalDocumentBody(DocumentNode document) {
		return document.toString();
	}



}
