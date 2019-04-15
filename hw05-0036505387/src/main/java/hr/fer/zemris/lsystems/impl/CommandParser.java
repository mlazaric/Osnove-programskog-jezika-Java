package hr.fer.zemris.lsystems.impl;

import java.util.Objects;

import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;

import static hr.fer.zemris.lsystems.impl.ParserUtil.*;

/**
 * Helper class for parsing commands from string.
 * 
 * @author Marko Lazarić
 *
 */
public final class CommandParser {

	/**
	 * This class should not be instanced.
	 */
	private CommandParser() {}
	
	/**
	 * Parses a command from the given string.
	 * 
	 * @param command the command to parse
	 * @return the parsed command
	 * 
	 * @throws IllegalArgumentException if the string does not represent a valid command
	 * @throws NullPointerException if {@code command} is {@code null}
	 */
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

}
