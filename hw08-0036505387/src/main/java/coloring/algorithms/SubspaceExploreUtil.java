package coloring.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Helper class for exploring subspaces using BFS, DFS or BFSv.
 *
 * @author Marko Lazarić
 */
public final class SubspaceExploreUtil {

    private SubspaceExploreUtil() {} // This class should not be instanced.

    /**
     * Uses breadth-first search to explore the subspace of all valid states.
     *
     * @param s0 the starting state
     * @param process the processor for valid states
     * @param succ the generator for successors
     * @param acceptable the tester for validity of states
     * @param <S> the type of the state
     */
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

    /**
     * Uses Depth-first search to explore the subspace of all valid states.
     *
     * @param s0 the starting state
     * @param process the processor for valid states
     * @param succ the generator for successors
     * @param acceptable the tester for validity of states
     * @param <S> the type of the state
     */
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

    /**
     * Uses improved breadth-first search to explore the subspace of all valid states.
     *
     * @param s0 the starting state
     * @param process the processor for valid states
     * @param succ the generator for successors
     * @param acceptable the tester for validity of states
     * @param <S> the type of the state
     */
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
