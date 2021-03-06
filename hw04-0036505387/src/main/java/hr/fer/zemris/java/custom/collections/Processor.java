package hr.fer.zemris.java.custom.collections;

/**
 * Models an object capable of performing some operation on the passed object.
 *
 * @author Marko Lazarić
 *
 * @param <T> type of object to process
 *
 */
@FunctionalInterface
public interface Processor<T> {

	/**
	 * Performs an operation on the {@code value}.
	 *
	 * @param value the object to process
	 */
	void process(T value);

}
