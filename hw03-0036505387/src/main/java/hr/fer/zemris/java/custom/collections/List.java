package hr.fer.zemris.java.custom.collections;

/**
 * Implements an abstract list.
 *
 * @author Marko Lazarić
 *
 */
public interface List extends Collection {

	/**
	 * Returns the object at the specified index.
	 *
	 * @param index the index of the element within the list
	 * @return the object at the specified index
	 *
	 * @throws IndexOutOfBoundsException if {@code index} is not a valid index
	 */
	Object get(int index);

	/**
	 * Inserts the value at the specified position in the collection.
	 *
	 * @param value the value to insert
	 * @param position the position to insert at
	 *
	 * @throws NullPointerException if {@code value} is {@code null}
	 * @throws IndexOutOfBoundsException if {@code position} is not a valid position
	 */
	void insert(Object value, int position);

	/**
	 * Returns the index of the element in the collection. If the element is not in the
	 * collection, it returns -1.
	 *
	 * @param value the object to find
	 * @return the index of the object in the collection or -1
	 */
	int indexOf(Object value);

	/**
	 * Removes the element at the specified index from the collection.
	 *
	 * @param index the index of the element
	 *
	 * @throws IndexOutOfBoundsException if {@code index} is not a valid index
	 */
	void remove(int index);

}
