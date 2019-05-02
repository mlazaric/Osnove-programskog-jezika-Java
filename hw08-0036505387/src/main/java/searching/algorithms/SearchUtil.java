package searching.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SearchUtil {

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
