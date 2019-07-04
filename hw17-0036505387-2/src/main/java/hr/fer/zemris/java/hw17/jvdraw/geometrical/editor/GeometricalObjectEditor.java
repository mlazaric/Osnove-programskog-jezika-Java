package hr.fer.zemris.java.hw17.jvdraw.geometrical.editor;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;

import javax.swing.*;

public abstract class GeometricalObjectEditor<G extends GeometricalObject> extends JPanel {

    protected final G geometricalObject;

    public GeometricalObjectEditor(G geometricalObject) {
        this.geometricalObject = geometricalObject;
    }

    public abstract void checkEditing();
    public abstract void acceptEditing();

}
