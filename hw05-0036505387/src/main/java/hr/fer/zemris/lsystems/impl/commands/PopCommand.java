package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Command to pop the top {@link TurtleState} from the {@link Context}.
 *
 * @author Marko Lazarić
 *
 */
public class PopCommand implements Command {

	/**
	 * Pops the current {@link TurtleState} from the {@link Context}.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context, Painter painter) {
		context.popState();
	}

	/**
	 * <b>Notice:</b> {@link PopCommand} has no state, so all instances of it
	 * can be considered equal.<br/>
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return 0;
	}

	/**
	 * <b>Notice:</b> {@link PopCommand} has no state, so all instances of it
	 * can be considered equal.<br/>
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PopCommand)) {
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "PopCommand []";
	}

}
