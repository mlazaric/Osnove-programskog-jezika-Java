package hr.fer.zemris.java.custom.scripting.exec;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

    void accept(T t, U u, V v);

}
