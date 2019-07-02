package hr.fer.zemris.java.hw17.jvdraw.geometrical.visitors;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.*;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.Line;

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
