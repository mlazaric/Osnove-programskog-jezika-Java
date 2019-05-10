package searching.algorithms;

import java.util.Objects;

/**
 * Models a simple node represented by its state, cost and parent.
 *
 * @param <S> the type of the state
 *
 * @author Marko Lazarić
 */
public class Node<S> {

    /**
     * The state represented by this {@link Node}.
     */
    private final S state;

    /**
     * The cost to get to this {@link Node}.
     */
    private final double cost;

    /**
     * The parent of this {@link Node}.
     */
    private final Node<S> parent;

    /**
     * Creates a {@link Node} with the specified arguments.
     *
     * @param parent the parent of the node
     * @param state the state represented by the node
     * @param cost the cost to get to the node
     *
     * @throws NullPointerException if {@code state} is {@code null}
     */
    public Node(Node<S> parent, S state, double cost) {
        this.state = Objects.requireNonNull(state, "State cannot be null.");
        this.cost = cost;
        this.parent = parent;
    }

    /**
     * Returns the state represented by this {@link Node}.
     *
     * @return the state represented by this {@link Node}
     */
    public S getState() {
        return state;
    }

    /**
     * Returns the cost to get to this {@link Node}.
     *
     * @return the cost to get to this {@link Node}
     */
    public double getCost() {
        return cost;
    }

    /**
     * Returns the parent of this {@link Node}.
     *
     * @return the parent of this {@link Node}
     */
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
