package hr.fer.zemris.java.hw17.jvdraw.drawing;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;

import javax.swing.*;
import java.util.Objects;

public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {

    private final DrawingModel model;

    public DrawingObjectListModel(DrawingModel model) {
        this.model = Objects.requireNonNull(model, "Drawing model cannot be null.");
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
