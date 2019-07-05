package hr.fer.zemris.java.hw17.jvdraw.geometrical.object;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.LineEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectVisitor;

import java.awt.*;
import java.util.Objects;

/**
 * A {@link GeometricalObject} which represents a line modelled with its color, start and end coordinates.
 *
 * @author Marko Lazarić
 */
public class Line extends GeometricalObject {

    /**
     * The starting point of the line.
     */
    private Point start;

    /**
     * The ending point of the line.
     */
    private Point end;

    /**
     * The color of the line.
     */
    private Color color;

    /**
     * Creates a {@link Line} with the given arguments.
     *
     * @param start the starting point of the line
     * @param end the ending point of the line
     * @param color the color of the line
     */
    public Line(Point start, Point end, Color color) {
        this.start = Objects.requireNonNull(start, "Starting point cannot be null.");
        this.end = Objects.requireNonNull(end, "End point cannot be null.");
        this.color = Objects.requireNonNull(color, "Color cannot be null.");
    }

    /**
     * Creates a "blank" {@link Line}.
     */
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

    @Override
    public GeometricalObjectEditor createGeometricalObjectEditor() {
        return new LineEditor(this);
    }

    /**
     * Returns the starting point of the line.
     *
     * @return the starting point of the line
     */
    public Point getStart() {
        return start;
    }

    /**
     * Sets the starting point of the line to the given argument.
     *
     * @param start the new value of {@link #start}
     */
    public void setStart(Point start) {
        this.start = start;
        fireGeometricalObjectChanged();
    }

    /**
     * Returns the ending point of the line.
     *
     * @return the ending point of the line
     */
    public Point getEnd() {
        return end;
    }

    /**
     * Sets the ending point of the line to the given argument.
     *
     * @param end the new value of {@link #end}
     */
    public void setEnd(Point end) {
        this.end = end;
        fireGeometricalObjectChanged();
    }

    /**
     * Returns the color of the line.
     *
     * @return the color of the line
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the line to the given argument.
     *
     * @param color the new value of {@link #color}
     */
    public void setColor(Color color) {
        this.color = color;
        fireGeometricalObjectChanged();
    }

}
