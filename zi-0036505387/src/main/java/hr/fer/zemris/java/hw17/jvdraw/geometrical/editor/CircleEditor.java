package hr.fer.zemris.java.hw17.jvdraw.geometrical.editor;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorArea;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;

import javax.swing.*;
import java.awt.*;

/**
 * A concrete {@link GeometricalObjectEditor} for {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle}.
 *
 * @param <C> the specific type of Circle
 *
 * @author Marko Lazarić
 */
public class CircleEditor<C extends Circle> extends GeometricalObjectEditor<C> {

    /**
     * The numeric values of the center x and y coordinates and the radius of the circle.
     */
    private final JSpinner centerX, centerY, radius;

    /**
     * The color to use for the outline of the circle.
     */
    private final ColorArea outlineColor;

    /**
     * Creates a new {@link CircleEditor} with the given argument.
     *
     * @param geometricalObject the {@link Circle} to edit
     */
    public CircleEditor(C geometricalObject) {
        super(geometricalObject);

        centerX = new JSpinner();
        centerY = new JSpinner();
        radius = new JSpinner();
        outlineColor = new ColorArea(geometricalObject.getOutlineColor());

        centerX.setValue(geometricalObject.getCenter().x);
        centerY.setValue(geometricalObject.getCenter().y);
        radius.setValue(geometricalObject.getRadius());

        initGUI();
    }

    /**
     * Initialises the editor GUI.
     */
    private void initGUI() {
        setLayout(new GridLayout(0, 4));

        add(new JLabel("Center x: ", JLabel.RIGHT));
        add(centerX);
        add(new JLabel("Center y: ", JLabel.RIGHT));
        add(centerY);

        add(new JLabel("Radius: ", JLabel.RIGHT));
        add(radius);
        add(new JLabel("Outline color: ", JLabel.RIGHT));
        add(outlineColor);
    }

    @Override
    public void checkEditing() {
    }

    @Override
    public void acceptEditing() {
        geometricalObject.setCenter(new Point((Integer) centerX.getValue(), (Integer) centerY.getValue()));
        geometricalObject.setRadius((Integer) radius.getValue());
        geometricalObject.setOutlineColor(outlineColor.getCurrentColor());
    }

}
