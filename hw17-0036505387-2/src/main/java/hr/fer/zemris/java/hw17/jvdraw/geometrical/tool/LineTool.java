package hr.fer.zemris.java.hw17.jvdraw.geometrical.tool;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;

import java.awt.*;
import java.awt.event.MouseEvent;

public class LineTool extends AbstractTool {

    private Line line;

    public LineTool(DrawingModel model, IColorProvider foreground, IColorProvider background, JDrawingCanvas canvas) {
        super(model, foreground, background, canvas);

        line = new Line();
    }

    @Override
    public void paint(Graphics2D g2d) {
        line.setColor(foreground.getCurrentColor());

        if (line.isValid()) {
            line.paint(g2d);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (line.getStart() == null) {
            line.setStart(e.getPoint());
        }
        else {
            line.setEnd(e.getPoint());

            model.add(line);
            line = new Line();

            canvas.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        line.setEnd(e.getPoint());
        canvas.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        line.setEnd(e.getPoint());
        canvas.repaint();
    }

}
