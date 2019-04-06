package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.ObjectStack;

public class Context {

	private ObjectStack<TurtleState> stack;

	public Context() {
		stack = new ObjectStack<>();
	}

	public TurtleState getCurrentState() {
		return stack.peek();
	}

	public void pushState(TurtleState state) {
		stack.push(state);
	}

	public void popState() {
		stack.pop();
	}

}
