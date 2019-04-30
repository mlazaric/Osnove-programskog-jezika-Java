package searching.slagalica;

import searching.algorithms.Transition;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Slagalica implements Supplier<KonfiguracijaSlagalice>,
                                  Function<KonfiguracijaSlagalice,
                                  List<Transition<KonfiguracijaSlagalice>>>, Predicate<KonfiguracijaSlagalice> {

    private final KonfiguracijaSlagalice startingConfiguration;

    public Slagalica(KonfiguracijaSlagalice startingConfiguration) {
        this.startingConfiguration = Objects.requireNonNull(startingConfiguration, "Starting configuration cannot be null.");
    }


    @Override
    public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice konfiguracijaSlagalice) {
        return null;
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
