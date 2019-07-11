package hr.fer.zemris.java.hw17.jvdraw.geometrical.tool;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledTriangle;

import java.awt.*;
import java.awt.event.MouseEvent;

public class FilledTriangleTool extends AbstractTool {

    private FilledTriangle filledTriangle;


    public FilledTriangleTool(DrawingModel model, IColorProvider foreground, IColorProvider background, JDrawingCanvas canvas) {
        super(model, foreground, background, canvas);

        filledTriangle = new FilledTriangle();
    }

    @Override
    public void paint(Graphics2D g2d) {
        if (filledTriangle.isValid()) {
            filledTriangle.paint(g2d);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (filledTriangle.getA() == null) {
            filledTriangle.setA(e.getPoint());

            filledTriangle.setOutlineColor(foreground.getCurrentColor());
            filledTriangle.setFillColor(background.getCurrentColor());
        }
        else if (filledTriangle.getB() == null) {
            filledTriangle.setB(e.getPoint());
        }
        else {
            filledTriangle.setC(e.getPoint());

            model.add(filledTriangle);
            filledTriangle = new FilledTriangle();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
