package hr.fer.zemris.java.hw17.jvdraw.geometrical;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectVisitor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Models a drawable geometrical object.
 *
 * @author Marko Lazarić
 */
public abstract class GeometricalObject {

    /**
     * The list of notifiers that should be called when the {@link GeometricalObject} is modified.
     */
    private List<GeometricalObjectListener> listeners = new ArrayList<>();

    /**
     * Returns the bounding box of the {@link GeometricalObject}.
     *
     * @return the bounding box of the {@link GeometricalObject}
     */
    public abstract Rectangle getBoundingBox();

    /**
     * Visits the {@link GeometricalObject} using the passed visitor.
     *
     * @param v the visitor to visit the {@link GeometricalObject} with
     */
    public abstract void accept(GeometricalObjectVisitor v);

    /**
     * Paints the {@link GeometricalObject} using the passed graphics.
     *
     * @param graphics the graphics to use to paint the {@link GeometricalObject} with
     */
    public abstract void paint(Graphics2D graphics);

    /**
     * Returns whether the {@link GeometricalObject} is valid (whether it can be painted).
     *
     * @return true if the {@link GeometricalObject} is valid, false otherwise
     */
    public abstract boolean isValid();

    /**
     * {@inheritDoc}
     */
    public abstract String toString();

    /**
     * Creates an {@link GeometricalObjectEditor} for this {@link GeometricalObject}.
     *
     * @return the newly created {@link GeometricalObjectEditor}
     */
    public abstract GeometricalObjectEditor createGeometricalObjectEditor();

    /**
     * Adds a listener to geometrical object changes.
     *
     * @param l the listener to add
     */
    public void addGeometricalObjectListener(GeometricalObjectListener l) {
        listeners.add(l);
    }

    /**
     * Removes a listener.
     *
     * @param l the listener to remove
     */
    public void removeGeometricalObjectListener(GeometricalObjectListener l) {
        listeners.add(l);
    }

    /**
     * Fires a geometrical object change event to all registered listeners.
     */
    protected void fireGeometricalObjectChanged() {
        listeners.forEach(l -> l.geometricalObjectChanged(this));
    }

}
