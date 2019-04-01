package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * A simple demonstration of {@link SmartScriptParser}.
 *
 * @author Marko Lazarić
 *
 */
public class SmartScriptTester {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Program requires exactly one argument.");
			return;
		}

		String docBody;

		try {
			docBody = new String(
					Files.readAllBytes(Paths.get(args[0])),
					StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			System.out.format("Could not open '%s'.%n", args[0]);
			return;
		}


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
