package hr.fer.zemris.lsystems.impl.commands;

public class DrawCommand extends MoveCommand {

	public DrawCommand(double step) {
		super(step, true);
	}

	@Override
	public String toString() {
		return "DrawCommand [toString()=" + super.toString() + "]";
	}

}
