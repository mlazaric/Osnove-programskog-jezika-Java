package searching.algorithms;

import java.util.Objects;

public class Node<S> {

    private final S state;
    private final double cost;
    private final Node<S> parent;

    public Node(Node<S> parent, S state, double cost) {
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

    @Override
    public String toString() {
        return "Node{" +
                "state=" + state +
                ", cost=" + cost +
                ", parent=" + parent +
                '}';
    }
}
