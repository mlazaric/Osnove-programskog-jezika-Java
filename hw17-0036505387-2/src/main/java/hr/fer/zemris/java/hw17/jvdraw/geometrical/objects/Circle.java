package hr.fer.zemris.java.hw17.jvdraw.geometrical.objects;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;

import java.awt.*;

public class Circle extends GeometricalObject {

    protected Point center;
    protected int radius;

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
        graphics.drawOval(center.x, center.y, radius, radius);
    }

    @Override
    public String toString() {
        return "Circle (" + center.x + "," + center.y + "), " + radius;
    }
}
