package searching.algorithms;

import java.util.Objects;

public class Node<S> {

    private final S state;
    private final double cost;
    private final Node<S> parent;

    public Node(S state, double cost, Node<S> parent) {
        this.state = Objects.requireNonNull(state, "State cannot be null.");
        this.cost = cost;
        this.parent = parent;
    }

    public S getState() {
        return state;
    }

    public double getCost() {
        return cost;
    }

    public Node<S> getParent() {
        return parent;
    }
}
