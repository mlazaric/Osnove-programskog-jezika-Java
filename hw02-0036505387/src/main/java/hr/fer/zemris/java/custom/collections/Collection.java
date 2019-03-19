package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Models a simple "abstract" collection.
 *
 * @author Marko Lazarić
 *
 */
public class Collection {

	/**
	 * Creates a new collection.
	 */
	protected Collection() {}

	/**
	 * Returns whether the collection is empty.
	 *
	 * @return true if there are no elements in the collection,
	 *         false otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns the number of elements in the collection.
	 *
	 * @return the number of elements in the collection
	 */
	public int size() {
		return 0;
	}

	/**
	 * Adds the {@code value} to the collection.
	 *
	 * @param value the object to add to the collection
	 */
	public void add(Object value) {}

	/**
	 * Checks whether {@code value} is in the collection.
	 *
	 * @param value the object to find
	 * @return true if it is in the collection,
	 *         false otherwise
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Removes the {@code value} from the collection.
	 *
	 * @param value the object to be removed
	 * @return true if it has been removed,
	 *         false otherwise
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Returns an array containing all the elements in the collection.
	 *
	 * @return array containing all the elements in the collection
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Performs {@code processor.process(element)} on every element in the
	 * collection.
	 *
	 * @param processor the processor used to process the elements
	 */
	public void forEach(Processor processor) {}

	/**
	 * Adds all the elements of the passed collection to this collection.
	 *
	 * @param other a collection whose elements should be added to this collection
	 *
	 * @throws NullPointerException if {@code other} is {@code null}
	 */
	public void addAll(Collection other) {
		Objects.requireNonNull(other);

		class AddAllProcessor extends Processor {

			@Override
			public void process(Object value) {
				add(value);
			}

		}

		other.forEach(new AddAllProcessor());
	}

	/**
	 * Clears all elements from the collection.
	 */
	public void clear() {}

}
