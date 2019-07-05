package hr.fer.zemris.java.hw17.jvdraw.geometrical.object;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.FilledCircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectVisitor;

import java.awt.*;
import java.util.Objects;

/**
 * A {@link GeometricalObject} which represents a filled circle modelled with its center, radius, outline and fill colors.
 *
 * @author Marko Lazarić
 */
public class FilledCircle extends Circle {

    /**
     * The color to fill the circle with.
     */
    private Color fillColor;

    /**
     * Creates a new {@link FilledCircle} with the given arguments.
     *
     * @param center the coordinate of the center of the circle
     * @param radius the radius of the circle
     * @param outlineColor the color of the outline
     * @param fillColor the color to fill the circle with
     *
     * @throws NullPointerException if any argument is null
     */
    public FilledCircle(Point center, int radius, Color outlineColor, Color fillColor) {
        super(center, radius, outlineColor);
        this.fillColor = Objects.requireNonNull(fillColor, "Fill color cannot be null.");
    }

    /**
     * Creates a new "blank" {@link FilledCircle}.
     */
    public FilledCircle() {}

    @Override
    public void accept(GeometricalObjectVisitor v) {
        v.visit(this);
    }

    @Override
    public void paint(Graphics2D graphics) {
        graphics.setColor(fillColor);
        graphics.fillOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);

        super.paint(graphics);
    }

    @Override
    public GeometricalObjectEditor createGeometricalObjectEditor() {
        return new FilledCircleEditor(this);
    }

    @Override
    public boolean isValid() {
        return fillColor != null && super.isValid();
    }

    @Override
    public String toString() {
        return "Filled circle (" + center.x + "," + center.y + "), " + radius + ", " +
                String.format("#%02X%02X%02X", fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue());
    }

    /**
     * Returns the color to fill the circle with.
     *
     * @return the color to fill the circle with
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * Sets the color to fill the circle with to the given argument.
     *
     * @param fillColor the new value for {@link #fillColor}
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        fireGeometricalObjectChanged();
    }

}
