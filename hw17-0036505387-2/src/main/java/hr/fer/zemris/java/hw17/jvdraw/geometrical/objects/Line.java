package hr.fer.zemris.java.hw17.jvdraw.geometrical.objects;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;

import java.awt.*;
import java.util.Objects;

public class Line extends GeometricalObject {

    private Point start;
    private Point end;
    private Color color;

    public Line(Point start, Point end, Color color) {
        this.start = Objects.requireNonNull(start, "Starting point cannot be null.");
        this.end = Objects.requireNonNull(end, "End point cannot be null.");
        this.color = Objects.requireNonNull(color, "Color cannot be null.");
    }

    public Line() {}

    @Override
    public Rectangle getBoundingBox() {
        Rectangle boundingBox = new Rectangle(start);

        boundingBox.add(end);

        return boundingBox;
    }

    @Override
    public void accept(GeometricalObjectVisitor v) {
        v.visit(this);
    }

    @Override
    public void paint(Graphics2D graphics) {
        graphics.setColor(color);
        graphics.drawLine(start.x, start.y, end.x, end.y);
    }

    @Override
    public boolean isValid() {
        return start != null && end != null && color != null;
    }

    @Override
    public String toString() {
        return "Line (" + start.x + "," + start.y + ")-(" + end.x + "," + end.y + ")";
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
