package hr.fer.zemris.lsystems.impl.commands;

/**
 * Command to move a certain length without drawing a line.
 *
 * @author Marko Lazarić
 *
 */
public class SkipCommand extends MoveCommand {

	/**
	 * Creates a new {@link SkipCommand} with the given {@code step}.
	 *
	 * @param step the length to skip
	 */
	public SkipCommand(double step) {
		super(step, false);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "SkipCommand [toString()=" + super.toString() + "]";
	}

}
