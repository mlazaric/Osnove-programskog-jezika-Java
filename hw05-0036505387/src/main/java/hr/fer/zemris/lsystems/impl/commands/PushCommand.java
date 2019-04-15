package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Command to push a copy of the top {@link TurtleState} to the {@link Context}.
 *
 * @author Marko Lazarić
 *
 */
public class PushCommand implements Command {

	/**
	 * Pushes a copy of the current {@link TurtleState} to the {@link Context}.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context, Painter painter) {
		context.pushState(context.getCurrentState().copy());
	}

	/**
	 * <b>Notice:</b> {@link PushCommand} has no state, so all instances of it
	 * can be considered equal.<br/>
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return 0;
	}

	/**
	 * <b>Notice:</b> {@link PushCommand} has no state, so all instances of it
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
		if (!(obj instanceof PushCommand)) {
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "PushCommand []";
	}

}
