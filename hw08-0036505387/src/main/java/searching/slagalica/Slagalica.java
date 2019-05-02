package searching.slagalica;

import searching.algorithms.Transition;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Models a simple searcher for a solution to a {@link Slagalica} puzzle.
 *
 * @author Marko Lazarić
 */
public class Slagalica implements Supplier<KonfiguracijaSlagalice>,
                                  Function<KonfiguracijaSlagalice,
                                  List<Transition<KonfiguracijaSlagalice>>>, Predicate<KonfiguracijaSlagalice> {

    /**
     * The starting configuration of the puzzle.
     */
    private final KonfiguracijaSlagalice startingConfiguration;

    /**
     * Creates a new {@link Slagalica} with the specified arguments.
     *
     * @param startingConfiguration the starting configuration of the puzzle
     *
     * @throws NullPointerException if {@code startingConfiguration} is {@code null}
     */
    public Slagalica(KonfiguracijaSlagalice startingConfiguration) {
        this.startingConfiguration = Objects.requireNonNull(startingConfiguration, "Starting configuration cannot be null.");
    }

    /**
     * Returns all puzzle configurations reachable using one move.
     *
     * @param konfiguracijaSlagalice the current configuration
     * @return list of all the configurations reachable using exactly one move
     */
    @Override
    public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice konfiguracijaSlagalice) {
        return Stream.of(konfiguracijaSlagalice.moveLeft(),
                         konfiguracijaSlagalice.moveRight(),
                         konfiguracijaSlagalice.moveUp(),
                         konfiguracijaSlagalice.moveDown())
                      .filter(k -> !konfiguracijaSlagalice.equals(k)) // Remove duplicates
                      .map(k -> new Transition<>(k, 1))
                      .collect(Collectors.toList());
    }

    /**
     * Checks whether the puzzle configuration is solved.
     *
     * @param konfiguracijaSlagalice the puzzle configuration to check
     * @return true if it is solved, false otherwise
     */
    @Override
    public boolean test(KonfiguracijaSlagalice konfiguracijaSlagalice) {
        return KonfiguracijaSlagalice.SOLVED_CONFIGURATION.equals(konfiguracijaSlagalice);
    }

    /**
     * Returns the starting puzzle configuration
     *
     * @return the starting puzzle configuration
     */
    @Override
    public KonfiguracijaSlagalice get() {
        return startingConfiguration;
    }
}
