package hr.fer.zemris.java.hw17.jvdraw.geometrical.object;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.FilledTriangleEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectVisitor;

import java.awt.*;

public class FilledTriangle extends GeometricalObject {

    private Point a, b, c;
    private Color outlineColor, fillColor;

    public FilledTriangle() {}

    public FilledTriangle(Point point, Point point1, Point point2, Color color, Color color1) {
        a = point;
        b = point1;
        c = point2;
        outlineColor = color;
        fillColor = color1;
    }

    @Override
    public Rectangle getBoundingBox() {
        Polygon poly = new Polygon();

        poly.addPoint(a.x, a.y);
        poly.addPoint(b.x, b.y);
        poly.addPoint(c.x, c.y);

        Rectangle rect = poly.getBounds();

        // For some reason poly.getBounds() is sometimes off by 1
        rect.grow(2, 2);
        rect.translate(-1, -1);

        return rect;
    }

    @Override
    public void accept(GeometricalObjectVisitor v) {
        v.visit(this);
    }

    @Override
    public void paint(Graphics2D graphics) {
        Polygon poly = new Polygon();

        poly.addPoint(a.x, a.y);
        poly.addPoint(b.x, b.y);
        poly.addPoint(c.x, c.y);

        graphics.setColor(fillColor);
        graphics.fillPolygon(poly);
        graphics.setColor(outlineColor);
        graphics.drawPolygon(poly);
    }

    @Override
    public boolean isValid() {
        return a != null && b != null && c != null;
    }

    @Override
    public String toString() {
        return "Filled triangle (" + a.x + "," + a.y + "), " +
                "(" + b.x + "," + b.y + "), " +
                "(" + c.x + "," + c.y + "), " + ", " +
                String.format("#%02X%02X%02X", fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue());
    }

    @Override
    public GeometricalObjectEditor createGeometricalObjectEditor() {
        return new FilledTriangleEditor(this);
    }

    public Point getA() {
        return a;
    }

    public void setA(Point a) {
        this.a = a;
    }

    public Point getB() {
        return b;
    }

    public void setB(Point b) {
        this.b = b;
    }

    public Point getC() {
        return c;
    }

    public void setC(Point c) {
        this.c = c;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
}
