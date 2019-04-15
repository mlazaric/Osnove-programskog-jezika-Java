package hr.fer.zemris.lsystems.impl.commands;

import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Abstract command to move a certain length and <i>optionally</i> draw a line.
 *
 * @author Marko Lazarić
 *
 */
public abstract class MoveCommand implements Command {

	/**
	 * The length it should move.
	 */
	private final double step;

	/**
	 * Whether it should draw a line while moving.
	 */
	private final boolean drawLine;

	/**
	 * Creates a new {@link MoveCommand} with the given arguments.
	 *
	 * @param step the length to move
	 * @param drawLine whether it should draw a line while moving
	 */
	public MoveCommand(double step, boolean drawLine) {
		this.step = step;
		this.drawLine = drawLine;
	}

	/**
	 * Moves the current {@link TurtleState} to {@link #step} amount and <i>optionall</i> draws a line.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Context context, Painter painter) {
		TurtleState currentState = context.getCurrentState();

		Vector2D currentPosition = currentState.getPosition();
		Vector2D delta = currentState.getHeading().scaled(currentState.getScaler() * step);
		Vector2D nextPosition = currentPosition.translated(delta);

		if (drawLine) {
			painter.drawLine(currentPosition.getX(), currentPosition.getY(), nextPosition.getX(), nextPosition.getY(), currentState.getColor(), 1f);
		}

		currentState.setPosition(nextPosition);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode() {
		return Objects.hash(drawLine, step);
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
		if (!(obj instanceof MoveCommand)) {
			return false;
		}

		MoveCommand other = (MoveCommand) obj;

		return drawLine == other.drawLine && Double.doubleToLongBits(step) == Double.doubleToLongBits(other.step);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "MoveCommand [step=" + step + ", drawLine=" + drawLine + "]";
	}

}
