package searching.slagalica;

import java.util.Arrays;
import java.util.Objects;

public class KonfiguracijaSlagalice {

    public static final KonfiguracijaSlagalice SOLVED_CONFIGURATION = new KonfiguracijaSlagalice(new int[]{
            1, 2, 3,
            4, 5, 6,
            7, 8, 9});

    private final int[] configuration;

    public KonfiguracijaSlagalice(int[] configuration) {
        this.configuration = Objects.requireNonNull(configuration, "Configuration cannot be null.");
    }

    public int[] getConfiguration() {
        return Arrays.copyOf(configuration, configuration.length);
    }

    public int indexOfSpace() {
        for (int index = 0; index < configuration.length; ++index) {
            if (configuration[index] == 0) {
                return index;
            }
        }

        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KonfiguracijaSlagalice that = (KonfiguracijaSlagalice) o;

        return Arrays.equals(configuration, that.configuration);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(configuration);
    }
}
