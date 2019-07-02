package hr.fer.zemris.java.hw17.jvdraw.geometrical.objects;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;

import java.awt.*;
import java.util.Objects;

public class FilledCircle extends Circle {

    private Color fillColor;

    public FilledCircle(Color fillColor) {
        this.fillColor = Objects.requireNonNull(fillColor, "Fill color cannot be null.");
    }

    @Override
    public void accept(GeometricalObjectVisitor v) {
        v.visit(this);
    }

    @Override
    public void paint(Graphics2D graphics) {
        graphics.setColor(fillColor);
        graphics.fillOval(center.x, center.y, radius, radius);
    }

    @Override
    public String toString() {
        return super.toString() + ", " +
                String.format("#%02X%02X%02X", fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue());
    }
}
