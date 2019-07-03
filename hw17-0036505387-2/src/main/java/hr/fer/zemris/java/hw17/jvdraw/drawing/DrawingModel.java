package hr.fer.zemris.java.hw17.jvdraw.drawing;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;

public interface DrawingModel extends Iterable<GeometricalObject> {

    int getSize();

    GeometricalObject getObject(int index);

    void add(GeometricalObject object);

    void remove(GeometricalObject object);

    void changeOrder(GeometricalObject object, int offset);

    int indexOf(GeometricalObject object);

    void clear();

    void clearModifiedFlag();

    boolean isModified();

    void addDrawingModelListener(DrawingModelListener l);

    void removeDrawingModelListener(DrawingModelListener l);

}
