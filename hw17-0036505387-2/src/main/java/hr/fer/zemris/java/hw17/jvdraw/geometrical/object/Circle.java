package hr.fer.zemris.java.hw17.jvdraw.geometrical.object;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.CircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectVisitor;

import java.awt.*;
import java.util.Objects;

public class Circle extends GeometricalObject {

    protected Point center;
    protected int radius;
    protected Color outlineColor;

    public Circle(Point center, int radius, Color outlineColor) {
        this.center = Objects.requireNonNull(center, "Center cannot be null.");
        this.radius = radius;
        this.outlineColor = Objects.requireNonNull(outlineColor, "Outline color cannot be null.");
    }

    public Circle() {}

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(center.x - radius, center.y - radius, radius, radius);
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

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }
}
