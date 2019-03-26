package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	void testParsingDocument1() throws IOException {
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
	void testParsingDocument2() throws IOException {
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
	void testParsingDocument3() throws IOException {
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
	void testParsingDocument4() throws IOException {
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

}
