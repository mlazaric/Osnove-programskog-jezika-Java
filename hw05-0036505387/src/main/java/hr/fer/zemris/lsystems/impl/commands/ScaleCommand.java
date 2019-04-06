package hr.fer.zemris.lsystems.impl.commands;

import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

public class ScaleCommand implements Command {

	private final double factor;

	public ScaleCommand(double factor) {
		this.factor = factor;
	}

	@Override
	public void execute(Context context, Painter painter) {
		context.getCurrentState().setScaler(factor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(factor);
	}

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



}
