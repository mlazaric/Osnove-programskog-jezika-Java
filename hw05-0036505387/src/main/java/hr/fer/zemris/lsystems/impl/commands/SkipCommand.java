package hr.fer.zemris.lsystems.impl.commands;

public class SkipCommand extends MoveCommand {

	public SkipCommand(double step) {
		super(step, false);
	}

	@Override
	public String toString() {
		return "SkipCommand [toString()=" + super.toString() + "]";
	}

}
