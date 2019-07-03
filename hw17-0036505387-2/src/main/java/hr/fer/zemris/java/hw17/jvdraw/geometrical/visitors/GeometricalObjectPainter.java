package hr.fer.zemris.java.hw17.jvdraw.geometrical.visitors;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.Line;

import java.awt.*;
import java.util.Objects;

public class GeometricalObjectPainter implements GeometricalObjectVisitor {

    private final Graphics2D graphics;

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

}
