package hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;

/**
 * Models an object which can visit a {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject}.
 *
 * @author Marko Lazarić
 */
public interface GeometricalObjectVisitor {

    /**
     * Visits a line.
     *
     * @param line the line to visit
     */
    void visit(Line line);

    /**
     * Visit a circle.
     *
     * @param circle the circle to visit
     */
    void visit(Circle circle);

    /**
     * Visits a filled circle.
     *
     * @param filledCircle the filled circle to visit
     */
    void visit(FilledCircle filledCircle);

    void visit(FilledTriangle filledTriangle);

}
