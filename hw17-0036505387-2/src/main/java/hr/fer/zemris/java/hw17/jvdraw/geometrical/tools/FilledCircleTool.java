package hr.fer.zemris.java.hw17.jvdraw.geometrical.tools;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.FilledCircle;

import java.awt.*;
import java.awt.event.MouseEvent;

public class FilledCircleTool extends AbstractTool {

    private FilledCircle filledCircle;

    public FilledCircleTool(DrawingModel model, IColorProvider foreground, IColorProvider background, JDrawingCanvas canvas) {
        super(model, foreground, background, canvas);

        filledCircle = new FilledCircle();
    }

    @Override
    public void paint(Graphics2D g2d) {
        filledCircle.setOutlineColor(foreground.getCurrentColor());
        filledCircle.setFillColor(foreground.getCurrentColor());

        if (filledCircle.isValid()) {
            filledCircle.paint(g2d);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (filledCircle.getCenter() == null) {
            filledCircle.setCenter(e.getPoint());
        }
        else {
            filledCircle.setRadius((int) filledCircle.getCenter().distance(e.getPoint()));

            model.add(filledCircle);
            filledCircle = new FilledCircle();

            canvas.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        if (filledCircle.getCenter() != null) {
            filledCircle.setRadius((int) filledCircle.getCenter().distance(e.getPoint()));
            canvas.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (filledCircle.getCenter() != null) {
            filledCircle.setRadius((int) filledCircle.getCenter().distance(e.getPoint()));
            canvas.repaint();
        }
    }

}
