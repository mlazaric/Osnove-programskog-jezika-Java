package hr.fer.zemris.lsystems.impl.commands;

import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Command to rotate the turtle's heading.
 *
 * @author Marko Lazarić
 *
 */
public class RotateCommand implements Command {

	/**
	 * The angle to rotate the turtle's heading, in degrees.
	 */
	private final double angle;

	/**
	 * Creates a new {@link RotateCommand} with the given argument.
	 *
	 * @param angle the angle to rotate the turtle's heading (in degrees)
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}

	/**
	 * Rotates the heading of the current {@link TurtleState} to {@link #angle} degrees.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context, Painter painter) {
		context.getCurrentState().getHeading().rotate(Math.toRadians(angle));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode() {
		return Objects.hash(angle);
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
		if (!(obj instanceof RotateCommand)) {
			return false;
		}

		RotateCommand other = (RotateCommand) obj;

		return Double.doubleToLongBits(angle) == Double.doubleToLongBits(other.angle);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "RotateCommand [angle=" + angle + "]";
	}
}
