package hr.fer.zemris.lsystems.impl.commands;

import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Command to scale the turtle's movement.
 *
 * @author Marko Lazarić
 *
 */
public class ScaleCommand implements Command {

	/**
	 * The factor to scale the turtle's movement with.
	 */
	private final double factor;

	/**
	 * Creates a new {@link ScaleCommand} with the given argument.
	 *
	 * @param factor the factor to scale the turtle's movement with
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}

	@Override
	public void execute(Context context, Painter painter) {
		context.getCurrentState().setScaler(factor);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode() {
		return Objects.hash(factor);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ScaleCommand)) {
			return false;
		}

		ScaleCommand other = (ScaleCommand) obj;

		return Double.doubleToLongBits(factor) == Double.doubleToLongBits(other.factor);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "ScaleCommand [factor=" + factor + "]";
	}

}
