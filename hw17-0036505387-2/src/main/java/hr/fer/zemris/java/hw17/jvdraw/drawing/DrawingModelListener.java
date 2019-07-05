package hr.fer.zemris.java.hw17.jvdraw.drawing;

/**
 * Models a listener which is notified of events fired by {@link DrawingModel}.
 *
 * @author Marko Lazarić
 */
public interface DrawingModelListener {

    /**
     * Fired when one or more objects have been added to the model.
     *
     * @param source the source of the event
     * @param firstIndex the index of the first {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} that was added
     * @param lastIndex the index of the last {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} that was added
     */
    void objectsAdded(DrawingModel source, int firstIndex, int lastIndex);

    /**
     * Fired when one or more objects have been removed from the model.
     *
     * @param source the source of the event
     * @param firstIndex the index of the first {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} that was removed
     * @param lastIndex the index of the last {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} that was removed
     */
    void objectsRemoved(DrawingModel source, int firstIndex, int lastIndex);

    /**
     * Fired when one or more objects have been changed within the model.
     *
     * @param source the source of the event
     * @param firstIndex the index of the first {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} that was changed
     * @param lastIndex the index of the last {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} that was changed
     */
    void objectsChanged(DrawingModel source, int firstIndex, int lastIndex);

}
