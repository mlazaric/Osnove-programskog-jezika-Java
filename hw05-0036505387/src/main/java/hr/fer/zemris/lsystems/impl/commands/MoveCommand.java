package hr.fer.zemris.lsystems.impl.commands;

import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

public abstract class MoveCommand implements Command {

	private final double step;
	private final boolean drawLine;

	public MoveCommand(double step, boolean drawLine) {
		this.step = step;
		this.drawLine = drawLine;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(drawLine, step);
	}

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

}
