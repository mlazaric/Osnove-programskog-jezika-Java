package hr.fer.zemris.java.hw17.jvdraw.geometrical.editor;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorArea;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;

import javax.swing.*;
import java.awt.*;

public class CircleEditor<C extends Circle> extends GeometricalObjectEditor<C> {

    private final JSpinner centerX, centerY, radius;
    private final ColorArea outlineColor;

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
