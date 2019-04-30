package coloring.algorithms;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SubspaceExploreUtil {

    public static <S> void bfs(
            Supplier<S> s0,
            Consumer<S> process,
            Function<S, List<S>> succ,
            Predicate<S> acceptable) {
        List<S> toProcess = new LinkedList<>();

        toProcess.add(s0.get());

        while (!toProcess.isEmpty()) {
            S current = toProcess.get(0);
            toProcess.remove(0);

            if (acceptable.test(current)) {
                process.accept(current);

                List<S> successors = succ.apply(current);
                toProcess.addAll(successors);
            }
        }
    }

    public static <S> void dfs(
            Supplier<S> s0,
            Consumer<S> process,
            Function<S,List<S>> succ,
            Predicate<S> acceptable) {
        List<S> toProcess = new LinkedList<>();

        toProcess.add(s0.get());

        while (!toProcess.isEmpty()) {
            S current = toProcess.get(0);
            toProcess.remove(0);

            if (acceptable.test(current)) {
                process.accept(current);

                List<S> successors = succ.apply(current);
                toProcess.addAll(0, successors);
            }
        }
    }

    public static <S> void bfsv(
            Supplier<S> s0,
            Consumer<S> process,
            Function<S,List<S>> succ,
            Predicate<S> acceptable) {
        List<S> toProcess = new LinkedList<>();
        Set<S> visited = new HashSet<>();

        toProcess.add(s0.get());
        visited.add(s0.get());

        while (!toProcess.isEmpty()) {
            S current = toProcess.get(0);
            toProcess.remove(0);

            if (acceptable.test(current)) {
                process.accept(current);

                List<S> successors = succ.apply(current);
                successors.removeAll(visited);

                visited.addAll(successors);
                toProcess.addAll(successors);
                System.out.println(toProcess.size());
            }
        }
    }
}
