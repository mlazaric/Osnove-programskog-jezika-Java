package searching.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Helper class for searching through state trees using bfs or bfsv.
 *
 * @author Marko Lazarić
 */
public final class SearchUtil {

    private SearchUtil() {} // This class should not be instanced.

    /**
     * Uses breadth-first search to find a state which satisfies the {@code goal} predicate.
     *
     * @param s0 the starting state
     * @param succ the generator for successors
     * @param goal the tester for validity of states
     * @param <S> the type of the state
     * @return the state which goal returns true for, or null if no such state has been found
     */
    public static <S> Node<S> bfs(
            Supplier<S> s0,
            Function<S, List<Transition<S>>> succ,
            Predicate<S> goal) {
        List<Node<S>> toExplore = new LinkedList<>();

        toExplore.add(new Node<>(null, s0.get(), 0));

        while (!toExplore.isEmpty()) {
            Node<S> current = toExplore.get(0);

            toExplore.remove(0);

            if (goal.test(current.getState())) {
                return current;
            }

            for (Transition<S> successors : succ.apply(current.getState())) {
                toExplore.add(new Node<>(current, successors.getState(), current.getCost() + successors.getCost()));
            }
        }

        return null;
    }

    /**
     * Uses improved breadth-first search to find a state which satisfies the {@code goal} predicate.
     *
     * @param s0 the starting state
     * @param succ the generator for successors
     * @param goal the tester for validity of states
     * @param <S> the type of the state
     * @return the state which goal returns true for, or null if no such state has been found
     */
    public static <S> Node<S> bfsv(
            Supplier<S> s0,
            Function<S, List<Transition<S>>> succ,
            Predicate<S> goal) {
        List<Node<S>> toExplore = new LinkedList<>();
        Set<S> visited = new HashSet<>();

        toExplore.add(new Node<>(null, s0.get(), 0));
        visited.add(s0.get());

        while (!toExplore.isEmpty()) {
            Node<S> current = toExplore.get(0);

            toExplore.remove(0);

            if (goal.test(current.getState())) {
                return current;
            }

            List<S> succesors = succ.apply(current.getState())
                                    .stream()
                                    .filter(t -> !visited.contains(t.getState()))
                                    .map(Transition::getState)
                                    .collect(Collectors.toList());

            List<Node<S>> nodes = succ.apply(current.getState())
                                      .stream()
                                      .filter(t -> !visited.contains(t.getState()))
                                      .map(t -> new Node<>(current, t.getState(), current.getCost() + t.getCost()))
                                      .collect(Collectors.toList());

            visited.addAll(succesors);
            toExplore.addAll(nodes);
        }

        return null;
    }

}
