package hr.fer.zemris.java.hw17.jvdraw.geometrical.objects;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;

import java.awt.*;
import java.util.Objects;

public class FilledCircle extends Circle {

    private Color fillColor;

    public FilledCircle(Point center, int radius, Color outlineColor, Color fillColor) {
        super(center, radius, outlineColor);
        this.fillColor = Objects.requireNonNull(fillColor, "Fill color cannot be null.");
    }

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
    public boolean isValid() {
        return fillColor != null && super.isValid();
    }

    @Override
    public String toString() {
        return super.toString() + ", " +
                String.format("#%02X%02X%02X", fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue());
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
}
