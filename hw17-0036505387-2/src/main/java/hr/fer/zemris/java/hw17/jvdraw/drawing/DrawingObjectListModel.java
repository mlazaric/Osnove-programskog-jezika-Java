package hr.fer.zemris.java.hw17.jvdraw.drawing;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;

import javax.swing.*;
import java.util.Objects;

/**
 * An implementation of {@link ListModel} which keeps track of the drawn {@link GeometricalObject}s.
 *
 * @author Marko Lazarić
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {

    /**
     * The model to adapt.
     */
    private final DrawingModel model;

    /**
     * Creates a new {@link DrawingObjectListModel} with the given argument.
     *
     * @param model the model to adapt
     *
     * @throws NullPointerException if the argument is null
     */
    public DrawingObjectListModel(DrawingModel model) {
        this.model = Objects.requireNonNull(model, "Drawing model cannot be null.");

        this.model.addDrawingModelListener(this);
    }

    @Override
    public int getSize() {
        return model.getSize();
    }

    @Override
    public GeometricalObject getElementAt(int index) {
        return model.getObject(index);
    }

    @Override
    public void objectsAdded(DrawingModel source, int firstIndex, int lastIndex) {
        fireIntervalAdded(this, firstIndex, lastIndex);
    }

    @Override
    public void objectsRemoved(DrawingModel source, int firstIndex, int lastIndex) {
        fireIntervalRemoved(this, firstIndex, lastIndex);
    }

    @Override
    public void objectsChanged(DrawingModel source, int firstIndex, int lastIndex) {
        fireContentsChanged(this, firstIndex, lastIndex);
    }
}
