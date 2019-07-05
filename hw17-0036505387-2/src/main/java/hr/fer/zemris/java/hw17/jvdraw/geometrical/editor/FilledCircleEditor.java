package hr.fer.zemris.java.hw17.jvdraw.geometrical.editor;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorArea;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;

import javax.swing.*;

/**
 * A concrete {@link GeometricalObjectEditor} for {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle}.
 *
 * @author Marko Lazarić
 */
public class FilledCircleEditor extends CircleEditor<FilledCircle> {

    /**
     * The color to fill the circle with.
     */
    private final ColorArea fillColor;

    /**
     * Creates a new {@link FilledCircleEditor} with the given argument.
     *
     * @param geometricalObject the {@link FilledCircle} to edit
     */
    public FilledCircleEditor(FilledCircle geometricalObject) {
        super(geometricalObject);

        fillColor = new ColorArea(geometricalObject.getFillColor());

        initGUI();
    }

    /**
     * Initialises the editor GUI.
     */
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
