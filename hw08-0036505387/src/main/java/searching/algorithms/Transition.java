package searching.algorithms;

import java.util.Objects;

public class Transition<S> {

    private final S state;
    private final double cost;

    public Transition(S state, double cost) {
        this.state = Objects.requireNonNull(state, "State cannot be null.");
        this.cost = cost;
    }

    public S getState() {
        return state;
    }

    public double getCost() {
        return cost;
    }
}
