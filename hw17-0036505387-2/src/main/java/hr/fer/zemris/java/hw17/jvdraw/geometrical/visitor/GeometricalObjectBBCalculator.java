package hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;

import java.awt.*;

public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {

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

    private void updateBoundingBox(GeometricalObject geometricalObject) {
        Rectangle otherBoundingBox = geometricalObject.getBoundingBox();

        if (boundingBox == null) {
            boundingBox = otherBoundingBox;
        }
        else {
            boundingBox = boundingBox.union(otherBoundingBox);
        }
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}
