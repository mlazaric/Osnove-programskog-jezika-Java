package hr.fer.zemris.java.hw17.jvdraw.drawing;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;

/**
 * Represents the model for the list of {@link GeometricalObject}s and their drawing.
 *
 * @author Marko Lazarić
 */
public interface DrawingModel extends Iterable<GeometricalObject> {

    /**
     * Returns the number of {@link GeometricalObject}s stored in the model.
     *
     * @return the number of {@link GeometricalObject}s stored in the model
     */
    int getSize();

    /**
     * Returns the {@link GeometricalObject} at the specified index.
     *
     * @param index the index of the {@link GeometricalObject}
     * @return the {@link GeometricalObject} at the specified index
     *
     * @throws IndexOutOfBoundsException if the index is smaller than 0 or larger than or equal to the {@link #getSize()}
     */
    GeometricalObject getObject(int index);

    /**
     * Adds a {@link GeometricalObject} to the model and notifies the listeners of a change.
     *
     * @param object the {@link GeometricalObject} to add
     */
    void add(GeometricalObject object);

    /**
     * Removes the {@link GeometricalObject} from the model and notifies the listeners of a change.
     *
     * @param object the {@link GeometricalObject} to remove
     */
    void remove(GeometricalObject object);

    /**
     * Moves the {@link GeometricalObject} up (if the offset is negative) or down (if the offset is positive) specified
     * number of places and notifies the listeners of a change.
     *
     * @param object the {@link GeometricalObject} to move
     * @param offset the number of places to move it up or down
     *
     * @throws IndexOutOfBoundsException if {@link #indexOf(GeometricalObject)} + offset is not a valid index
     */
    void changeOrder(GeometricalObject object, int offset);

    /**
     * Returns the index of {@link GeometricalObject} or -1 if it is not in the model.
     *
     * @param object the {@link GeometricalObject} to find
     * @return the index of the {@link GeometricalObject} or -1
     */
    int indexOf(GeometricalObject object);

    /**
     * Removes all {@link GeometricalObject}s from the model and notifies the listeners of a change.
     */
    void clear();

    /**
     * Sets the modified flag to false.
     */
    void clearModifiedFlag();

    /**
     * Returns whether the model has been modified.
     *
     * @return true if the model has been modified, false otherwise
     */
    boolean isModified();

    /**
     * Adds a drawing listener.
     *
     * @param l the listener to add
     */
    void addDrawingModelListener(DrawingModelListener l);

    /**
     * Removes a drawing listener.
     *
     * @param l the listener to remove
     */
    void removeDrawingModelListener(DrawingModelListener l);

}
