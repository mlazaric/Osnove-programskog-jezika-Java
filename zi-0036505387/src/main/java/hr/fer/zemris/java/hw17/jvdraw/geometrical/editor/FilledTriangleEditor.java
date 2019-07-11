package hr.fer.zemris.java.hw17.jvdraw.geometrical.editor;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorArea;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledTriangle;

import javax.swing.*;
import java.awt.*;

public class FilledTriangleEditor extends GeometricalObjectEditor<FilledTriangle> {

    private final ColorArea outlineColor;
    private final ColorArea fillColor;

    public FilledTriangleEditor(FilledTriangle geometricalObject) {
        super(geometricalObject);

        outlineColor = new ColorArea(geometricalObject.getOutlineColor());
        fillColor = new ColorArea(geometricalObject.getFillColor());

        initGUI();
    }

    private void initGUI() {
        setLayout(new GridLayout(0, 2));

        add(new JLabel("Outline color: "));
        add(outlineColor);

        add(new JLabel("Fill color: "));
        add(fillColor);
    }

    @Override
    public void checkEditing() {
    }

    @Override
    public void acceptEditing() {
        geometricalObject.setFillColor(fillColor.getCurrentColor());
        geometricalObject.setOutlineColor(outlineColor.getCurrentColor());
    }

}
