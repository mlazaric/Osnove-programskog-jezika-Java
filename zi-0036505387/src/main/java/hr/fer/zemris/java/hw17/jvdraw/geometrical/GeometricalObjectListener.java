package hr.fer.zemris.java.hw17.jvdraw.geometrical;

/**
 * Models a listener which listen for changes to the {@link GeometricalObject}.
 *
 * @author Marko Lazarić
 */
public interface GeometricalObjectListener {

    /**
     * The method called when the {@link GeometricalObject} has been changed.
     *
     * @param o the changed {@link GeometricalObject}
     */
    void geometricalObjectChanged(GeometricalObject o);

}
