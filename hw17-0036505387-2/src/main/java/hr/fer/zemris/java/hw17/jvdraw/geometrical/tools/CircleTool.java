package hr.fer.zemris.java.hw17.jvdraw.geometrical.tools;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.Line;

import java.awt.*;
import java.awt.event.MouseEvent;

public class CircleTool extends AbstractTool {

    private Circle circle;

    public CircleTool(DrawingModel model, IColorProvider foreground, IColorProvider background, JDrawingCanvas canvas) {
        super(model, foreground, background, canvas);

        circle = new Circle();
    }

    @Override
    public void paint(Graphics2D g2d) {
        circle.setOutlineColor(foreground.getCurrentColor());

        if (circle.isValid()) {
            circle.paint(g2d);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (circle.getCenter() == null) {
            circle.setCenter(e.getPoint());
        }
        else {
            circle.setRadius((int) circle.getCenter().distance(e.getPoint()));

            model.add(circle);
            circle = new Circle();

            canvas.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        if (circle.getCenter() != null) {
            circle.setRadius((int) circle.getCenter().distance(e.getPoint()));
            canvas.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (circle.getCenter() != null) {
            circle.setRadius((int) circle.getCenter().distance(e.getPoint()));
            canvas.repaint();
        }
    }

}
