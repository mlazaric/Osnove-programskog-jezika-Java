package hr.fer.zemris.java.hw17.jvdraw.geometrical.editor;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;

import javax.swing.*;
import java.awt.*;

/**
 * A concrete {@link GeometricalObjectEditor} for {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line}.
 *
 * @author Marko Lazarić
 */
public class LineEditor extends GeometricalObjectEditor<Line> {

    /**
     * The numeric values of the start and end x and y coordinates.
     */
    private final JSpinner startX, startY, endX, endY;

    /**
     * Creates a new {@link LineEditor} with the given argument.
     *
     * @param geometricalObject the instance of the {@link Line} which it is editing
     */
    public LineEditor(Line geometricalObject) {
        super(geometricalObject);

        startX = new JSpinner();
        startY = new JSpinner();
        endX = new JSpinner();
        endY = new JSpinner();

        startX.setValue(geometricalObject.getStart().x);
        startY.setValue(geometricalObject.getStart().y);
        endX.setValue(geometricalObject.getEnd().x);
        endY.setValue(geometricalObject.getEnd().y);

        initGUI();
    }

    /**
     * Initialises the editor GUI.
     */
    private void initGUI() {
        setLayout(new GridLayout(0, 5));

        add(new JLabel("Start coordinates: "));
        add(new JLabel("x: ", JLabel.RIGHT));
        add(startX);
        add(new JLabel("y: ", JLabel.RIGHT));
        add(startY);

        add(new JLabel("End coordinates: "));
        add(new JLabel("x: ", JLabel.RIGHT));
        add(endX);
        add(new JLabel("y: ", JLabel.RIGHT));
        add(endY);
    }

    @Override
    public void checkEditing() {
    }

    @Override
    public void acceptEditing() {
        geometricalObject.setStart(new Point((Integer) startX.getValue(), (Integer) startY.getValue()));
        geometricalObject.setEnd(new Point((Integer) endX.getValue(), (Integer) endY.getValue()));
    }

}
