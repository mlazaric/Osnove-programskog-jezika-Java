package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Command to change the color of the turtle's pen.
 *
 * @author Marko Lazarić
 *
 */
public class ColorCommand implements Command {

	/**
	 * The new color of the turtle's pen.
	 */
	private final Color color;

	/**
	 * Creates a {@link ColorCommand} with the given {@link Color}.
	 *
	 * @param color the new color of the turtle's pen
	 *
	 * @throws NullPointerException if {@code color} is {@code null}
	 */
	public ColorCommand(Color color) {
		this.color = Objects.requireNonNull(color, "Color of the turtle's pen cannot be null.");
	}

	/**
	 * Changes the color of the current {@link TurtleState} to {@link #color}.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context, Painter painter) {
		context.getCurrentState().setColor(color);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode() {
		return Objects.hash(color);
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
		if (!(obj instanceof ColorCommand)) {
			return false;
		}

		ColorCommand other = (ColorCommand) obj;

		return Objects.equals(color, other.color);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "ColorCommand [color=" + color + "]";
	}

}
