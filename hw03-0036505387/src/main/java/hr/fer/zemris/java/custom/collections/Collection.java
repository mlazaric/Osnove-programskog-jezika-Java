package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Models a simple "abstract" collection.
 *
 * @author Marko Lazarić
 *
 */
public interface Collection {

	/**
	 * Returns whether the collection is empty.
	 *
	 * @return true if there are no elements in the collection,
	 *         false otherwise
	 */
	default boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns the number of elements in the collection.
	 *
	 * @return the number of elements in the collection
	 */
	int size();

	/**
	 * Adds the {@code value} to the collection.
	 *
	 * @param value the object to add to the collection
	 */
	void add(Object value);

	/**
	 * Checks whether {@code value} is in the collection.
	 *
	 * @param value the object to find
	 * @return true if it is in the collection,
	 *         false otherwise
	 */
	boolean contains(Object value);

	/**
	 * Removes the {@code value} from the collection.
	 *
	 * @param value the object to be removed
	 * @return true if it has been removed,
	 *         false otherwise
	 */
	boolean remove(Object value);

	/**
	 * Returns an array containing all the elements in the collection.
	 *
	 * @return array containing all the elements in the collection
	 */
	Object[] toArray();

	/**
	 * Performs {@code processor.process(element)} on every element in the
	 * collection.
	 *
	 * @param processor the processor used to process the elements
	 */
	default void forEach(Processor processor) {
		ElementsGetter getter = createElementsGetter();

		while (getter.hasNextElement()) {
			processor.process(getter.getNextElement());
		}
	}

	/**
	 * Adds all the elements of the passed collection to this collection.
	 *
	 * @param other a collection whose elements should be added to this collection
	 *
	 * @throws NullPointerException if {@code other} is {@code null}
	 */
	default void addAll(Collection other) {
		Objects.requireNonNull(other);

		class AddAllProcessor implements Processor {

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
	void clear();

	ElementsGetter createElementsGetter();

	default void addAllSatisfying(Collection col, Tester tester) {
		Objects.requireNonNull(col, "Collection cannot be null.");
		Objects.requireNonNull(tester, "Tester cannot be null.");

		col.forEach(new Processor() {

			@Override
			public void process(Object value) {
				if (tester.test(value)) {
					add(value);
				}
			}

		});
	}

}
