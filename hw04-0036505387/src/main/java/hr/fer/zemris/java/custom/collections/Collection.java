package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Models a simple abstract collection.
 *
 * @author Marko Lazarić
 *
 * @param <E> type of elements in the collection
 *
 */
public interface Collection<E> {

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
	void add(E value);

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
	default void forEach(Processor<? super E> processor) {
		ElementsGetter<E> getter = createElementsGetter();

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
	default void addAll(Collection<? extends E> other) {
		Objects.requireNonNull(other);

		class AddAllProcessor implements Processor<E> {

			@Override
			public void process(E value) {
				add(value);
			}

		}

		other.forEach(new AddAllProcessor());
	}

	/**
	 * Clears all elements from the collection.
	 */
	void clear();

	/**
	 * Creates a new {@link ElementsGetter}.
	 *
	 * @return the newly created {@link ElementsGetter}
	 */
	ElementsGetter<E> createElementsGetter();

	/**
	 * Adds all elements from {@code col} to this collection which
	 * satisfy {@code tester}'s condition.
	 *
	 * @param col the collection whose elements should be checked and
	 *            added to this one
	 * @param tester the tester used to check whether an element should
	 *               be added to this collection
	 *
	 * @throws if {@code col} or {@code tester} is null
	 */
	default void addAllSatisfying(Collection<? extends E> col, Tester<? super E> tester) {
		Objects.requireNonNull(col, "Collection cannot be null.");
		Objects.requireNonNull(tester, "Tester cannot be null.");

		col.forEach(new Processor<E>() {

			@Override
			public void process(E value) {
				if (tester.test(value)) {
					add(value);
				}
			}

		});
	}

}
