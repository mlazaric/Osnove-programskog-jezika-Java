package hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;

import java.awt.*;
import java.util.Objects;

/**
 * A {@link GeometricalObjectVisitor} which paints all the {@link GeometricalObject} it visits.
 *
 * @author Marko Lazarić
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {

    /**
     * The graphics used for painting the {@link GeometricalObject}.
     */
    private final Graphics2D graphics;

    /**
     * Creates a new {@link GeometricalObjectPainter} with the given argument.
     *
     * @param graphics the graphics used for painting the {@link GeometricalObject}
     *
     * @throws NullPointerException if the argument is null
     */
    public GeometricalObjectPainter(Graphics2D graphics) {
        this.graphics = Objects.requireNonNull(graphics, "Graphics cannot be null.");
    }

    @Override
    public void visit(Line line) {
        line.paint(graphics);
    }

    @Override
    public void visit(Circle circle) {
        circle.paint(graphics);
    }

    @Override
    public void visit(FilledCircle filledCircle) {
        filledCircle.paint(graphics);
    }

    @Override
    public void visit(FilledTriangle filledTriangle) {
        filledTriangle.paint(graphics);
    }

}
