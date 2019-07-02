package hr.fer.zemris.java.hw17.jvdraw.geometrical.objects;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;

import java.awt.*;

public class Line extends GeometricalObject {

    private Point start;
    private Point end;

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
        graphics.drawLine(start.x, start.y, end.x, end.y);
    }
}
