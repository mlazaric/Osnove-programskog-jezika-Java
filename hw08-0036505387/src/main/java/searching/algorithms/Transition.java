package searching.algorithms;

import java.util.Objects;

/**
 * Models a transition to a state.
 *
 * @param <S> the type of the state
 *
 * @author Marko Lazarić
 */
public class Transition<S> {

    /**
     * The state to which this transition points to.
     */
    private final S state;

    /**
     * The cost of the transition.
     */
    private final double cost;

    /**
     * Creates a new {@link Transition} with the specified parameters.
     *
     * @param state the state to which this transition points to
     * @param cost the cost of the transition
     *
     * @throws NullPointerException if {@code state} is {@code null}
     */
    public Transition(S state, double cost) {
        this.state = Objects.requireNonNull(state, "State cannot be null.");
        this.cost = cost;
    }

    /**
     * Returns the state to which this transition points to.
     *
     * @return the state to which this transition points to
     */
    public S getState() {
        return state;
    }

    /**
     * Returns the cost of the transition.
     *
     * @return the cost of the transition
     */
    public double getCost() {
        return cost;
    }
}
