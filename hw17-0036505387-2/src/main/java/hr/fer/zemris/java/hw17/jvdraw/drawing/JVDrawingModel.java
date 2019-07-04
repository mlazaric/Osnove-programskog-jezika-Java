package hr.fer.zemris.java.hw17.jvdraw.drawing;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class JVDrawingModel implements DrawingModel, GeometricalObjectListener {

    private final List<GeometricalObject> geometricalObjects = new ArrayList<>();
    private final List<DrawingModelListener> listeners = new ArrayList<>();

    private boolean isModified = false;

    @Override
    public int getSize() {
        return geometricalObjects.size();
    }

    @Override
    public GeometricalObject getObject(int index) {
        return geometricalObjects.get(index);
    }

    @Override
    public void add(GeometricalObject object) {
        // Add object
        geometricalObjects.add(object);

        // Add listener to it
        object.addGeometricalObjectListener(this);

        // Notify listeners of the new object
        listeners.stream()
                 .forEach(l -> l.objectsAdded(this, geometricalObjects.size() - 1, geometricalObjects.size() - 1));
        isModified = true;
    }

    @Override
    public void remove(GeometricalObject object) {
        int index = geometricalObjects.indexOf(object);

        if (index != -1) {
            // Remove object
            geometricalObjects.remove(object);

            // Remove listener from it
            object.removeGeometricalObjectListener(this);

            // Notify listeners of its removal
            listeners.stream()
                     .forEach(l -> l.objectsRemoved(this, index, index));
            isModified = true;
        }
    }

    @Override
    public void changeOrder(GeometricalObject object, int offset) {
        int index = geometricalObjects.indexOf(object);

        if (index == -1) {
            throw new IllegalArgumentException("GeometricalObject was not added to DrawingModel before changing its order.");
        }

        // Remove object from the old position
        geometricalObjects.remove(index);

        // Add it to the new position
        geometricalObjects.add(index + offset, object);

        // Notify listeners of a change to the order of the object
        listeners.stream()
                 .forEach(l -> l.objectsChanged(this, index, index + offset));
        isModified = true;
    }

    @Override
    public int indexOf(GeometricalObject object) {
        return geometricalObjects.indexOf(object);
    }

    @Override
    public void clear() {
        int lastIndex = geometricalObjects.size() - 1;

        // Remove listener from all the object
        geometricalObjects.stream()
                          .forEach(l -> l.removeGeometricalObjectListener(this));

        // Remove all object
        geometricalObjects.clear();

        // Notify the listeners of the removal of all object
        listeners.stream()
                 .forEach(l -> l.objectsRemoved(this, 0, lastIndex));
        isModified = true;
    }

    @Override
    public void clearModifiedFlag() {
        isModified = false;
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public void addDrawingModelListener(DrawingModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeDrawingModelListener(DrawingModelListener l) {
        listeners.remove(l);
    }

    @Override
    public void geometricalObjectChanged(GeometricalObject o) {
        int index = geometricalObjects.indexOf(o);

        if (index != -1) {
            listeners.stream()
                     .forEach(l -> l.objectsChanged(this, index, index));
        }
    }

    @Override
    public Iterator<GeometricalObject> iterator() {
        // Prevent modification through the iterator
        return Collections.unmodifiableCollection(geometricalObjects).iterator();
    }
}
