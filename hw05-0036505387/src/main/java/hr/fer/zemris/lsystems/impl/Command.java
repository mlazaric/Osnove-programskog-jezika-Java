package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Models a command that interacts with the {@link Context} and {@link Painter} to
 * perform some kind of action.
 *
 * @author Marko Lazarić
 *
 */
@FunctionalInterface
public interface Command {

	/**
	 * Performs the command.
	 *
	 * @param context the current context used for storing {@link TurtleState}
	 * @param painter the painter used for drawing
	 */
	void execute(Context context, Painter painter);

}
