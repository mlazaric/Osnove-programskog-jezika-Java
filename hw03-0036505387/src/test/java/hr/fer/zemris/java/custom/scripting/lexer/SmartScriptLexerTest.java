package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SmartScriptLexerTest {

	@Test
	void testEchoTagFromPDF() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$= i i * @sin \"0.000\" @decfmt $}");

		assertEquals(new SmartScriptToken(SmartScriptTokenType.TAG_START, "{$"), lexer.nextToken());

		lexer.setState(SmartScriptLexerState.INSIDE_TAG);

		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "="), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.OPERATOR, "*"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "@sin"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.STRING, "0.000"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "@decfmt"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.TAG_END, "$}"), lexer.nextToken());

		lexer.setState(SmartScriptLexerState.BASIC);

		assertEquals(new SmartScriptToken(SmartScriptTokenType.EOF, null), lexer.nextToken());

		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testBasicForLoopTagFromPDF() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ FOR i -1 10 1 $}");

		assertEquals(new SmartScriptToken(SmartScriptTokenType.TAG_START, "{$"), lexer.nextToken());

		lexer.setState(SmartScriptLexerState.INSIDE_TAG);

		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "FOR"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.INTEGER, -1), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.INTEGER, 10), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.INTEGER, 1), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.TAG_END, "$}"), lexer.nextToken());

		lexer.setState(SmartScriptLexerState.BASIC);

		assertEquals(new SmartScriptToken(SmartScriptTokenType.EOF, null), lexer.nextToken());

		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testSpacedOutForLoopTagFromPDF() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ FOR sco_re \"-1\"10 \"1\" $}");

		assertEquals(new SmartScriptToken(SmartScriptTokenType.TAG_START, "{$"), lexer.nextToken());

		lexer.setState(SmartScriptLexerState.INSIDE_TAG);

		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "FOR"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "sco_re"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.STRING, "-1"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.INTEGER, 10), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.STRING, "1"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.TAG_END, "$}"), lexer.nextToken());

		lexer.setState(SmartScriptLexerState.BASIC);

		assertEquals(new SmartScriptToken(SmartScriptTokenType.EOF, null), lexer.nextToken());

		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testSpacelessForLoopTagFromPDF() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ FOR i-1.35bbb\"1\" $}");

		assertEquals(new SmartScriptToken(SmartScriptTokenType.TAG_START, "{$"), lexer.nextToken());

		lexer.setState(SmartScriptLexerState.INSIDE_TAG);

		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "FOR"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.DOUBLE, -1.35), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "bbb"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.STRING, "1"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.TAG_END, "$}"), lexer.nextToken());

		lexer.setState(SmartScriptLexerState.BASIC);

		assertEquals(new SmartScriptToken(SmartScriptTokenType.EOF, null), lexer.nextToken());

		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testEscapingInEchoTag() {
		SmartScriptLexer lexer = new SmartScriptLexer("A tag follows {$= \"Joe \\\"Long\\\" Smith\"$}.");

		assertEquals(new SmartScriptToken(SmartScriptTokenType.TEXT, "A tag follows "), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.TAG_START, "{$"), lexer.nextToken());

		lexer.setState(SmartScriptLexerState.INSIDE_TAG);

		assertEquals(new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "="), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.STRING, "Joe \"Long\" Smith"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.TAG_END, "$}"), lexer.nextToken());

		lexer.setState(SmartScriptLexerState.BASIC);

		assertEquals(new SmartScriptToken(SmartScriptTokenType.TEXT, "."), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.EOF, null), lexer.nextToken());

		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testInvalidEscapingOutsideOfTags() {
		SmartScriptLexer lexer1 = new SmartScriptLexer("\\n");

		assertThrows(SmartScriptLexerException.class, () -> lexer1.nextToken());

		SmartScriptLexer lexer2 = new SmartScriptLexer("\\t");

		assertThrows(SmartScriptLexerException.class, () -> lexer2.nextToken());

		SmartScriptLexer lexer3 = new SmartScriptLexer("\\r");

		assertThrows(SmartScriptLexerException.class, () -> lexer3.nextToken());
	}

	@Test
	void testValidEscapingOfBackslashOutsideOfTags() {
		SmartScriptLexer lexer = new SmartScriptLexer("\\\\");

		assertEquals(new SmartScriptToken(SmartScriptTokenType.TEXT, "\\"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.EOF, null), lexer.nextToken());

		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testValidEscapingOfTagStartOutsideOfTags() {
		SmartScriptLexer lexer = new SmartScriptLexer("\\{");

		assertEquals(new SmartScriptToken(SmartScriptTokenType.TEXT, "{"), lexer.nextToken());
		assertEquals(new SmartScriptToken(SmartScriptTokenType.EOF, null), lexer.nextToken());

		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}

}