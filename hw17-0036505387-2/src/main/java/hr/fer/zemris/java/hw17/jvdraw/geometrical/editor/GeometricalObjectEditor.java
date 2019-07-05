package hr.fer.zemris.java.hw17.jvdraw.geometrical.editor;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;

import javax.swing.*;

/**
 * An abstract editor for a {@link GeometricalObject}.
 *
 * @param <G> the specific {@link GeometricalObject} type
 *
 * @author Marko Lazarić
 */
public abstract class GeometricalObjectEditor<G extends GeometricalObject> extends JPanel {

    /**
     * The instance of the {@link GeometricalObject} which it is editing.
     */
    protected final G geometricalObject;

    /**
     * Creates a new {@link GeometricalObjectEditor} with the given argument.
     *
     * @param geometricalObject the instance of the {@link GeometricalObjectEditor} which it is editing
     */
    public GeometricalObjectEditor(G geometricalObject) {
        this.geometricalObject = geometricalObject;
    }

    /**
     * Checks whether the user has entered valid values for the {@link GeometricalObject}.
     *
     * If one or more values are invalid, it throws an exception.
     */
    public abstract void checkEditing();

    /**
     * Writes the new values to the {@link GeometricalObject} instance.
     */
    public abstract void acceptEditing();

}
