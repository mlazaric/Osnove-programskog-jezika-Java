package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Models a consumer which takes three arguments.
 *
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <V> the type of the third argument
 *
 * @author Marko Lazarić
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {

    /**
     * Accepts three arguments.
     *
     * @param t the first argument
     * @param u the second argument
     * @param v the third argument
     */
    void accept(T t, U u, V v);

}
