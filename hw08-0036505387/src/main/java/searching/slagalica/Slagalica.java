package searching.slagalica;

import searching.algorithms.Transition;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Slagalica implements Supplier<KonfiguracijaSlagalice>,
                                  Function<KonfiguracijaSlagalice,
                                  List<Transition<KonfiguracijaSlagalice>>>, Predicate<KonfiguracijaSlagalice> {

    private final KonfiguracijaSlagalice startingConfiguration;

    public Slagalica(KonfiguracijaSlagalice startingConfiguration) {
        this.startingConfiguration = Objects.requireNonNull(startingConfiguration, "Starting configuration cannot be null.");
    }


    @Override
    public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice konfiguracijaSlagalice) {
        return Stream.of(konfiguracijaSlagalice.moveLeft(),
                         konfiguracijaSlagalice.moveRight(),
                         konfiguracijaSlagalice.moveUp(),
                         konfiguracijaSlagalice.moveDown())
                      .filter(k -> k != null) // Remove nulls
                      .filter(k -> !konfiguracijaSlagalice.equals(k)) // Remove duplicates
                      .map(k -> new Transition<>(k, 1))
                      .collect(Collectors.toList());
    }

    @Override
    public boolean test(KonfiguracijaSlagalice konfiguracijaSlagalice) {
        return KonfiguracijaSlagalice.SOLVED_CONFIGURATION.equals(konfiguracijaSlagalice);
    }

    @Override
    public KonfiguracijaSlagalice get() {
        return startingConfiguration;
    }
}
