package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;

public abstract class CommandParser {

	public static Command parse(String command) {
		Objects.requireNonNull(command, "Cannot parse a null command.");

		String[] parts = command.strip().split("\\s+");
		int numberOfArguments = parts.length - 1;

		switch (parts[0]) {
			case "draw":
				throwIfWrongNumberOfArguments(1, numberOfArguments);
				return new DrawCommand(parseDoubleOrThrow(parts[1]));
	
			case "skip":
				throwIfWrongNumberOfArguments(1, numberOfArguments);
				return new SkipCommand(parseDoubleOrThrow(parts[1]));
	
			case "scale":
				throwIfWrongNumberOfArguments(1, numberOfArguments);
				return new ScaleCommand(parseDoubleOrThrow(parts[1]));
	
			case "rotate":
				throwIfWrongNumberOfArguments(1, numberOfArguments);
				return new RotateCommand(parseDoubleOrThrow(parts[1]));
	
			case "push":
				throwIfWrongNumberOfArguments(0, numberOfArguments);
				return new PushCommand();
	
			case "pop":
				throwIfWrongNumberOfArguments(0, numberOfArguments);
				return new PopCommand();
	
			case "color":
				throwIfWrongNumberOfArguments(1, numberOfArguments);
				return new ColorCommand(parseColorOrThrow(parts[1]));
	
			default:
				throw new IllegalArgumentException("'" + command + "' is not a valid command");
		}
	}

	private static Color parseColorOrThrow(String color) {
		try {
			return Color.decode(color);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("'" + color + "' is not a valid color.");
		}
	}

	private static double parseDoubleOrThrow(String number) {
		try {
			return Double.parseDouble(number);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("'" + number + "' is not a valid double.");
		}
	}

	private static void throwIfWrongNumberOfArguments(int expectedNumberOfArguments, int realNumberOfArguments) {
		if (expectedNumberOfArguments != realNumberOfArguments) {
			throw new IllegalArgumentException("Invalid number of arguments passed, expected " +
					expectedNumberOfArguments + ", got " + realNumberOfArguments + ".");
		}
	}

}
