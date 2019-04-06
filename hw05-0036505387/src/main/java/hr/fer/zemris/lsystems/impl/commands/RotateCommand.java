package hr.fer.zemris.lsystems.impl.commands;

import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

public class RotateCommand implements Command {

	private final double angle;

	public RotateCommand(double angle) {
		this.angle = angle;
	}

	@Override
	public void execute(Context context, Painter painter) {
		context.getCurrentState().getHeading().rotate(Math.toRadians(angle));
	}

	@Override
	public int hashCode() {
		return Objects.hash(angle);
	}

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

	@Override
	public String toString() {
		return "RotateCommand [angle=" + angle + "]";
	}
}
