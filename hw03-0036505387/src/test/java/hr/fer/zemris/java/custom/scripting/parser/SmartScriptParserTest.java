package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

class SmartScriptParserTest {

	private static DocumentNode runParserOnFile(String filepath) throws IOException {
		String docBody = new String(
				Files.readAllBytes(Paths.get(filepath)),
				StandardCharsets.UTF_8);

		SmartScriptParser parser = null;

		parser = new SmartScriptParser(docBody);

		return parser.getDocumentNode();
	}

	@Test
	void testParseForLoopTagWithoutEndTag() throws IOException {
		assertThrows(SmartScriptParserException.class, () -> runParserOnFile("src/test/resources/invalid/forLoopWithoutEndTag.txt"));
	}

	@Test
	void testParseUndefinedTag() throws IOException {
		assertThrows(SmartScriptParserException.class, () -> runParserOnFile("src/test/resources/invalid/undefinedTagName.txt"));
	}

	@Test
	void testParseTooManyArgumentsToForLoop() throws IOException {
		assertThrows(SmartScriptParserException.class, () -> runParserOnFile("src/test/resources/invalid/tooManyArgumentsToForLoop.txt"));
	}

	@Test
	void testParseTooFewArgumentsToForLoop() throws IOException {
		assertThrows(SmartScriptParserException.class, () -> runParserOnFile("src/test/resources/invalid/tooFewArgumentsToForLoop.txt"));
	}

	@Test
	void testParseInvalidVariableName() throws IOException {
		assertThrows(SmartScriptParserException.class, () -> runParserOnFile("src/test/resources/invalid/invalidVariableName.txt"));
	}

	@Test
	void testReparsingDocument1() throws IOException {
		DocumentNode parsed = runParserOnFile("src/test/resources/document1.txt");

		assertEquals(parsed, new SmartScriptParser(parsed.toString()).getDocumentNode());
	}

	@Test
	void testReparsingDocument2() throws IOException {
		DocumentNode parsed = runParserOnFile("src/test/resources/document2.txt");

		assertEquals(parsed, new SmartScriptParser(parsed.toString()).getDocumentNode());
	}

	@Test
	void testReparsingDocument3() throws IOException {
		DocumentNode parsed = runParserOnFile("src/test/resources/document3.txt");

		assertEquals(parsed, new SmartScriptParser(parsed.toString()).getDocumentNode());
	}

	@Test
	void testReparsingDocument4() throws IOException {
		DocumentNode parsed = runParserOnFile("src/test/resources/document4.txt");

		assertEquals(parsed, new SmartScriptParser(parsed.toString()).getDocumentNode());
	}

	@Test
	void testReparsingDocument5() throws IOException {
		DocumentNode parsed = runParserOnFile("src/test/resources/document5.txt");

		assertEquals(parsed, new SmartScriptParser(parsed.toString()).getDocumentNode());
	}

	@Test
	void testReparsingDocument6() throws IOException {
		DocumentNode parsed = runParserOnFile("src/test/resources/document6.txt");

		assertEquals(parsed, new SmartScriptParser(parsed.toString()).getDocumentNode());
	}

	@Test
	void testParsingDocument1Manually() throws IOException {
		DocumentNode parsed = runParserOnFile("src/test/resources/document1.txt");

		DocumentNode expected = new DocumentNode();

		expected.addChildNode(new EchoNode(new Element[]{
				new ElementString("text/plain"),
				new ElementFunction("setMimeType")
		}));

		expected.addChildNode(new TextNode("This is sample text.\n"));

		ForLoopNode firstForLoop = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(1), new ElementConstantInteger(10), new ElementConstantInteger(1));

		firstForLoop.addChildNode(new TextNode("\nThis is "));

		firstForLoop.addChildNode(new EchoNode(new Element[] {
				new ElementVariable("i")
		}));

		firstForLoop.addChildNode(new TextNode("-th time this message is generated.\n"));

		expected.addChildNode(firstForLoop);
		expected.addChildNode(new TextNode("\n"));

		ForLoopNode secondForLoop = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(0), new ElementConstantInteger(10), new ElementConstantInteger(2));

		secondForLoop.addChildNode(new TextNode("\nsin("));

		secondForLoop.addChildNode(new EchoNode(new Element[] {
				new ElementVariable("i")
		}));

		secondForLoop.addChildNode(new TextNode("^2) = "));

		secondForLoop.addChildNode(new EchoNode(new Element[] {
				new ElementVariable("i"),
				new ElementVariable("i"),
				new ElementOperator("*"),
				new ElementFunction("sin"),
				new ElementString("0.000"),
				new ElementFunction("decfmt")
		}));

		secondForLoop.addChildNode(new TextNode("\n"));

		expected.addChildNode(secondForLoop);
		expected.addChildNode(new TextNode("\n"));

		assertEquals(expected, parsed);
	}

	@Test
	void testParsingDocument2Manually() throws IOException {
		DocumentNode parsed = runParserOnFile("src/test/resources/document2.txt");

		DocumentNode expected = new DocumentNode();

		expected.addChildNode(new EchoNode(new Element[] {
				new ElementString("text/plain"),
				new ElementFunction("setMimeType")
		}));

		expected.addChildNode(new TextNode("\nRačunam sumu brojeva:\n"));

		expected.addChildNode(new EchoNode(new Element[] {
				new ElementString("a="),
				new ElementString("a"),
				new ElementConstantInteger(0),
				new ElementFunction("paramGet"),
				new ElementString(", b="),
				new ElementString("b"),
				new ElementConstantInteger(0),
				new ElementFunction("paramGet"),
				new ElementString(", rezultat="),
				new ElementString("a"),
				new ElementConstantInteger(0),
				new ElementFunction("paramGet"),
				new ElementString("b"),
				new ElementConstantInteger(0),
				new ElementFunction("paramGet"),
				new ElementOperator("+")
		}));

		expected.addChildNode(new TextNode("\n"));

		assertEquals(expected, parsed);
	}

	@Test
	void testParsingDocument3Manually() throws IOException {
		DocumentNode parsed = runParserOnFile("src/test/resources/document3.txt");

		DocumentNode expected = new DocumentNode();

		expected.addChildNode(new EchoNode(new Element[] {
				new ElementString("text/plain"),
				new ElementFunction("setMimeType")
		}));

		expected.addChildNode(new TextNode("\nOvaj dokument pozvan je sljedeći broj puta:\n"));

		expected.addChildNode(new EchoNode(new Element[] {
				new ElementString("brojPoziva"),
				new ElementString("1"),
				new ElementFunction("pparamGet"),
				new ElementFunction("dup"),
				new ElementConstantInteger(1),
				new ElementOperator("+"),
				new ElementString("brojPoziva"),
				new ElementFunction("pparamSet")
		}));

		expected.addChildNode(new TextNode("\n"));

		assertEquals(expected, parsed);
	}

	@Test
	void testParsingDocument4Manually() throws IOException {
		DocumentNode parsed = runParserOnFile("src/test/resources/document4.txt");

		DocumentNode expected = new DocumentNode();

		expected.addChildNode(new EchoNode(new Element[] {
				new ElementString("text/plain"),
				new ElementFunction("setMimeType")
		}));

		expected.addChildNode(new TextNode("Prvih 10 fibonaccijevih brojeva je:\n"));

		expected.addChildNode(new EchoNode(new Element[] {
				new ElementString("0"),
				new ElementString("a"),
				new ElementFunction("tparamSet"),
				new ElementString("1"),
				new ElementString("b"),
				new ElementFunction("tparamSet"),
				new ElementString("0\r\n1\r\n")
		}));

		ForLoopNode forLoopNode = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(3), new ElementConstantInteger(10), new ElementConstantInteger(1));

		forLoopNode.addChildNode(new EchoNode(new Element[] {
				new ElementString("b"),
				new ElementString("0"),
				new ElementFunction("tparamGet"),
				new ElementFunction("dup"),
				new ElementString("a"),
				new ElementString("0"),
				new ElementFunction("tparamGet"),
				new ElementOperator("+"),
				new ElementString("b"),
				new ElementFunction("tparamSet"),
				new ElementString("a"),
				new ElementFunction("tparamSet"),
				new ElementString("b"),
				new ElementString("0"),
				new ElementFunction("tparamGet"),
				new ElementString("\r\n")
		}));

		expected.addChildNode(forLoopNode);

		expected.addChildNode(new TextNode("\n"));

		assertEquals(expected, parsed);
	}

	@Test
	void testParsingDocument5Manually() throws IOException {
		DocumentNode parsed = runParserOnFile("src/test/resources/document5.txt");

		DocumentNode expected = new DocumentNode();

		expected.addChildNode(new EchoNode(new Element[] {
				new ElementString("text/html"),
				new ElementFunction("setMimeType")
		}));

		expected.addChildNode(new TextNode("\n" +
				"<html>\n" +
				"<head>\n" +
				"<title>Tablica Fibonaccijevih brojeva</title>\n" +
				"</head>\n" +
				"<body>\n" +
				"<h1>Fibonaccijevi brojevi</h1>\n" +
				"<p>U nastavku je prikazana tablica prvih 10\n" +
				"Fibonaccijevih brojeva.</p>\n"));

		expected.addChildNode(new EchoNode(new Element[] {
				new ElementString("0"),
				new ElementString("a"),
				new ElementFunction("tparamSet"),
				new ElementString("1"),
				new ElementString("b"),
				new ElementFunction("tparamSet")
		}));

		expected.addChildNode(new TextNode("\n" +
				"<table>\n" +
				"<thead>\n" +
				"<tr><th>Redni broj</th><th>Fibonaccijev broj</th></tr>\n" +
				"</thead>\n" +
				"<tbody>\n" +
				"<tr><td>1</td><td>0</td></tr>\n" +
				"<tr><td>2</td><td>1</td></tr>\n"));

		ForLoopNode forLoopNode = new ForLoopNode(new ElementVariable("i"), new ElementConstantInteger(3), new ElementConstantInteger(10), new ElementConstantInteger(1));

		forLoopNode.addChildNode(new TextNode("\n"));

		forLoopNode.addChildNode(new EchoNode(new Element[] {
				new ElementString("b"),
				new ElementString("0"),
				new ElementFunction("tparamGet"),
				new ElementFunction("dup"),
				new ElementString("a"),
				new ElementString("0"),
				new ElementFunction("tparamGet"),
				new ElementOperator("+"),
				new ElementString("b"),
				new ElementFunction("tparamSet"),
				new ElementString("a"),
				new ElementFunction("tparamSet")
		}));

		forLoopNode.addChildNode(new TextNode("\n" +
				"<tr><td>"));

		forLoopNode.addChildNode(new EchoNode(new Element[] {
				new ElementVariable("i")
		}));

		forLoopNode.addChildNode(new TextNode("</td><td>"));

		forLoopNode.addChildNode(new EchoNode(new Element[] {
				new ElementString("b"),
				new ElementString("0"),
				new ElementFunction("tparamGet")
		}));

		forLoopNode.addChildNode(new TextNode("</td></tr>\n"));

		expected.addChildNode(forLoopNode);

		expected.addChildNode(new TextNode("\n" +
				"</tbody>\n" +
				"</table>\n" +
				"</body>\n" +
				"</html>\n"));

		assertEquals(expected, parsed);
	}

}
