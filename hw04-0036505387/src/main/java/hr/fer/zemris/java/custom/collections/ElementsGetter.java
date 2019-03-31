package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implements an abstract iterator.
 *
 * @author Marko Lazarić
 *
 */
public interface ElementsGetter<T> {

	/**
	 * Returns whether there is a next element.
	 *
	 * @return true if there is a next element,
	 *         false otherwise
	 */
	boolean hasNextElement();

	/**
	 * Returns the next element in the collection.
	 *
	 * @return the next element in the collection.
	 *
	 * @throws NoSuchElementException if there are no more elements left in the
	 *                                collection to iterate over
	 * @throws ConcurrentModificationException if the underlying collection
	 *                                         is modified while being iterated
	 *                                         over
	 */
	T getNextElement();

	/**
	 * Process the remaining elements (the elements {@link ElementsGetter} hasn't
	 * iterated over yet).
	 *
	 * @param p the processor to process the elements with
	 */
	default void processRemaining(Processor<T> p) {
		Objects.requireNonNull(p, "Cannot process using null.");

		while (hasNextElement()) {
			p.process(getNextElement());
		}
	}

}
