package hr.fer.zemris.java.custom.collections;

/**
 * Models an object capable of performing some operation on the passed object.
 *
 * @author Marko Lazarić
 *
 */
@FunctionalInterface
public interface Processor {

	/**
	 * Performs an operation on the {@code value}.
	 *
	 * @param value the object to process
	 */
	void process(Object value);

}
