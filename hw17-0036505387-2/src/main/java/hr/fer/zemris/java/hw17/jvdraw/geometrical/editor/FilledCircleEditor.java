package hr.fer.zemris.java.hw17.jvdraw.geometrical.editor;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorArea;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;

import javax.swing.*;

public class FilledCircleEditor extends CircleEditor<FilledCircle> {

    private final ColorArea fillColor;

    public FilledCircleEditor(FilledCircle geometricalObject) {
        super(geometricalObject);

        fillColor = new ColorArea(geometricalObject.getFillColor());

        initGUI();
    }

    private void initGUI() {
        add(new JLabel());
        add(new JLabel("Fill color: "));
        add(fillColor);

    }

    @Override
    public void checkEditing() {
        super.checkEditing();
    }

    @Override
    public void acceptEditing() {
        super.acceptEditing();

        geometricalObject.setFillColor(fillColor.getCurrentColor());
    }

}
