package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

public class ColorCommand implements Command {

	private final Color color;

	public ColorCommand(Color factor) {
		this.color = factor;
	}

	@Override
	public void execute(Context context, Painter painter) {
		context.getCurrentState().setColor(color);
	}

	@Override
	public int hashCode() {
		return Objects.hash(color);
	}

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

	@Override
	public String toString() {
		return "ColorCommand [color=" + color + "]";
	}

}
