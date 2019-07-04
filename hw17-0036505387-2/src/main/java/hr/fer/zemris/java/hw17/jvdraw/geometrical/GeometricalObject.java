package hr.fer.zemris.java.hw17.jvdraw.geometrical;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectVisitor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GeometricalObject {

    private List<GeometricalObjectListener> listeners = new ArrayList<>();

    public abstract Rectangle getBoundingBox();
    public abstract void accept(GeometricalObjectVisitor v);
    public abstract void paint(Graphics2D graphics);
    public abstract boolean isValid();
    public abstract String toString();
    public abstract GeometricalObjectEditor createGeometricalObjectEditor();

    public void addGeometricalObjectListener(GeometricalObjectListener l) {
        listeners.add(l);
    }

    public void removeGeometricalObjectListener(GeometricalObjectListener l) {
        listeners.add(l);
    }

    protected void fireGeometricalObjectChanged() {
        listeners.forEach(l -> l.geometricalObjectChanged(this));
    }

}
