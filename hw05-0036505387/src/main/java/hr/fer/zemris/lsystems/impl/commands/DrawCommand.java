package hr.fer.zemris.lsystems.impl.commands;

/**
 * Command to move a certain length and draw a line.
 *
 * @author Marko Lazarić
 *
 */
public class DrawCommand extends MoveCommand {

	/**
	 * Creates a new {@link DrawCommand} with the given {@code step}.
	 *
	 * @param step the length to move
	 */
	public DrawCommand(double step) {
		super(step, true);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "DrawCommand [toString()=" + super.toString() + "]";
	}

}
