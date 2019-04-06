package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;

class CommandParserTest {

	// Draw
	@Test
	void testParseDrawWithValidArgument() {
		assertEquals(new DrawCommand(55.33), CommandParser.parse("draw 55.33"));
	}

	@Test
	void testParseDrawWithTooManyArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("draw 1 2"));
	}

	@Test
	void testParseDrawWithNotEnoughArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("draw"));
	}

	@Test
	void testParseDrawWithNonNumericArgument() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("draw exception"));
	}

	// Skip
	@Test
	void testParseSkipWithValidArgument() {
		assertEquals(new SkipCommand(55.33), CommandParser.parse("skip 55.33"));
	}

	@Test
	void testParseSkipWithTooManyArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("skip 1 2"));
	}

	@Test
	void testParseSkipWithNotEnoughArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("skip"));
	}

	@Test
	void testParseSkipWithNonNumericArgument() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("skip exception"));
	}

	// Scale
	@Test
	void testParseScaleWithValidArgument() {
		assertEquals(new ScaleCommand(55.33), CommandParser.parse("scale 55.33"));
	}

	@Test
	void testParseScaleWithTooManyArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("scale 1 2"));
	}

	@Test
	void testParseScaleWithNotEnoughArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("scale"));
	}

	@Test
	void testParseScaleWithNonNumericArgument() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("scale exception"));
	}

	// Rotate
	@Test
	void testParseRotateWithValidArgument() {
		assertEquals(new RotateCommand(55.33), CommandParser.parse("rotate 55.33"));
	}

	@Test
	void testParseRotateWithTooManyArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("rotate 1 2"));
	}

	@Test
	void testParseRotateWithNotEnoughArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("rotate"));
	}

	@Test
	void testParseRotateWithNonNumericArgument() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("rotate exception"));
	}

	// Push
	@Test
	void testParsePushWithNoArguments() {
		assertEquals(new PushCommand(), CommandParser.parse("push"));
	}

	@Test
	void testParsePushWithTooManyArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("push 2"));
	}

	// Pop
	@Test
	void testParsePopWithNoArguments() {
		assertEquals(new PopCommand(), CommandParser.parse("pop"));
	}

	@Test
	void testParsePopWithTooManyArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("pop 2"));
	}

	// Color
	@Test
	void testParseColorWithValidArgument() {
		assertEquals(new ColorCommand(Color.decode("000000")), CommandParser.parse("color 000000"));
	}

	@Test
	void testParseColorWithTooManyArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("color 1 2"));
	}

	@Test
	void testParseColorWithNotEnoughArguments() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("color"));
	}

	@Test
	void testParseColorInvalidArgument() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("color exception"));
	}

	// Invalid commands
	@Test
	void testParseOfUndefinedCommand() {
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("colour"));
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("štefica"));
		assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("test"));
	}

}
