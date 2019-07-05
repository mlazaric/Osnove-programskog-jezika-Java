package hr.fer.zemris.java.hw17.jvdraw.geometrical.object;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.CircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectVisitor;

import java.awt.*;
import java.util.Objects;

/**
 * A {@link GeometricalObject} which represents an unfilled circle modelled with its center, radius and outline color.
 *
 * @author Marko Lazarić
 */
public class Circle extends GeometricalObject {

    /**
     * The coordinate of the center of the circle.
     */
    protected Point center;

    /**
     * The radius of the circle.
     */
    protected int radius;

    /**
     * The color of the outline.
     */
    protected Color outlineColor;

    /**
     * Creates a new {@link Circle} with the given arguments.
     *
     * @param center the coordinate of the center of the circle
     * @param radius the radius of the circle
     * @param outlineColor the color of the outline
     *
     * @throws NullPointerException if any argument is null
     */
    public Circle(Point center, int radius, Color outlineColor) {
        this.center = Objects.requireNonNull(center, "Center cannot be null.");
        this.radius = radius;
        this.outlineColor = Objects.requireNonNull(outlineColor, "Outline color cannot be null.");
    }

    /**
     * Creates a new "blank" {@link Circle}.
     */
    public Circle() {}

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
    }

    @Override
    public void accept(GeometricalObjectVisitor v) {
        v.visit(this);
    }

    @Override
    public void paint(Graphics2D graphics) {
        graphics.setColor(outlineColor);
        graphics.drawOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
    }

    @Override
    public boolean isValid() {
        return center != null && radius > 0 && outlineColor != null;
    }

    @Override
    public String toString() {
        return "Circle (" + center.x + "," + center.y + "), " + radius;
    }

    @Override
    public GeometricalObjectEditor createGeometricalObjectEditor() {
        return new CircleEditor(this);
    }

    /**
     * Returns the coordinate of the center of the circle.
     *
     * @return the coordinate of the center of the circle
     */
    public Point getCenter() {
        return center;
    }

    /**
     * Sets the coordinate of the center of the circle to the given argument.
     *
     * @param center the new value for {@link #center}
     */
    public void setCenter(Point center) {
        this.center = center;
        fireGeometricalObjectChanged();
    }

    /**
     * Returns the radius of the circle.
     *
     * @return the radius of the circle
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the circle to the given argument.
     *
     * @param radius the new value for {@link #radius}
     */
    public void setRadius(int radius) {
        this.radius = radius;
        fireGeometricalObjectChanged();
    }

    /**
     * Returns the color of the outline.
     *
     * @return the color of the outline
     */
    public Color getOutlineColor() {
        return outlineColor;
    }

    /**
     * Sets the color of the outline to the given argument.
     *
     * @param outlineColor the new value for {@link #outlineColor}
     */
    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        fireGeometricalObjectChanged();
    }
}
