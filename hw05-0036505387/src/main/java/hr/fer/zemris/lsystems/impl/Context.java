package hr.fer.zemris.lsystems.impl;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Models a stack based storage of {@link TurtleState}s for the {@link LSystemImpl}.
 *
 * @author Marko Lazarić
 *
 */
public class Context {

	/**
	 * The stack used for storing the {@link TurtleState}s.
	 */
	private ObjectStack<TurtleState> stack;

	/**
	 * Creates a new {@link Context}.
	 */
	public Context() {
		stack = new ObjectStack<>();
	}

	/**
	 * Returns the {@link TurtleState} at the top of the stack.
	 *
	 * @return the {@link TurtleState} at the top of the stack
	 */
	public TurtleState getCurrentState() {
		return stack.peek();
	}

	/**
	 * Pushes a new state to the top of the stack.
	 *
	 * @param state the new state to add to the stack
	 *
	 * @throws NullPointerException if {@code state} is {@code null}
	 */
	public void pushState(TurtleState state) {
		Objects.requireNonNull(state, "Cannot push null turtle state to context stack.");

		stack.push(state);
	}

	/**
	 * Pops a {@link TurtleState} from the context stack.
	 *
	 * @throws EmptyStackException if there are no states on the context stack
	 */
	public void popState() {
		stack.pop();
	}

}
