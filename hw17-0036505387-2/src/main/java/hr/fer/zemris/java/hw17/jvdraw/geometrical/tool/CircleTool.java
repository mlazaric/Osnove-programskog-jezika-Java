package hr.fer.zemris.java.hw17.jvdraw.geometrical.tool;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A concrete {@link Tool} for drawing a {@link Circle}.
 *
 * @author Marko Lazarić
 */
public class CircleTool extends AbstractTool {

    /**
     * The buffered circle used for keeping track of the information.
     */
    private Circle circle;


    /**
     * Creates a new {@link CircleTool} with the given arguments.
     *
     * @param model the model to draw the {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} on
     * @param foreground the color provider for the foreground
     * @param background the color provider for the background
     * @param canvas the canvas to draw the {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} with
     *
     * @throws NullPointerException if any argument is null
     */
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
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
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
