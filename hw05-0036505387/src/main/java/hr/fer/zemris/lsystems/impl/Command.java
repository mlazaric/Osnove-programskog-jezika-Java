package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

@FunctionalInterface
public interface Command {

	void execute(Context context, Painter painter);

}
