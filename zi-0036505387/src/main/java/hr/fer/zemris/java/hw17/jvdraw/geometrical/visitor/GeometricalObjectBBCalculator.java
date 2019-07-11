package hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;

import java.awt.*;

/**
 * A {@link GeometricalObjectVisitor} which calculates the minimal combined bounding box of all the {@link GeometricalObject}
 * it visits.
 *
 * @author Marko Lazarić
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {

    /**
     * The current bounding box.
     */
    private Rectangle boundingBox;

    @Override
    public void visit(Line line) {
        updateBoundingBox(line);
    }

    @Override
    public void visit(Circle circle) {
        updateBoundingBox(circle);
    }

    @Override
    public void visit(FilledCircle filledCircle) {
        updateBoundingBox(filledCircle);
    }

    @Override
    public void visit(FilledTriangle filledTriangle) {
        updateBoundingBox(filledTriangle);
    }

    /**
     * Update the bounding box so it contains the old bounding box as well as the new one.
     *
     * @param geometricalObject the {@link GeometricalObject} whose bounding box should be added to the current one
     */
    private void updateBoundingBox(GeometricalObject geometricalObject) {
        Rectangle otherBoundingBox = geometricalObject.getBoundingBox();

        if (boundingBox == null) {
            boundingBox = otherBoundingBox;
        }
        else {
            boundingBox = boundingBox.union(otherBoundingBox);
        }
    }

    /**
     * Returns the current bounding box.
     *
     * @return the current bounding box
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

}
