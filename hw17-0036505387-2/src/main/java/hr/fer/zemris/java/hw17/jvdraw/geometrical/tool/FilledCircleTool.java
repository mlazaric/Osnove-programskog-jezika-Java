package hr.fer.zemris.java.hw17.jvdraw.geometrical.tool;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A concrete {@link Tool} for drawing a {@link FilledCircle}.
 *
 * @author Marko Lazarić
 */
public class FilledCircleTool extends AbstractTool {

    /**
     * The buffered circle used for keeping track of the information.
     */
    private FilledCircle filledCircle;

    /**
     * Creates a new {@link FilledCircleTool} with the given arguments.
     *
     * @param model the model to draw the {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} on
     * @param foreground the color provider for the foreground
     * @param background the color provider for the background
     * @param canvas the canvas to draw the {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} with
     *
     * @throws NullPointerException if any argument is null
     */
    public FilledCircleTool(DrawingModel model, IColorProvider foreground, IColorProvider background, JDrawingCanvas canvas) {
        super(model, foreground, background, canvas);

        filledCircle = new FilledCircle();
    }

    @Override
    public void paint(Graphics2D g2d) {
        filledCircle.setOutlineColor(foreground.getCurrentColor());
        filledCircle.setFillColor(background.getCurrentColor());

        if (filledCircle.isValid()) {
            filledCircle.paint(g2d);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (filledCircle.getCenter() == null) {
            filledCircle.setCenter(e.getPoint());
        }
        else {
            filledCircle.setRadius((int) filledCircle.getCenter().distance(e.getPoint()));
            filledCircle.setOutlineColor(foreground.getCurrentColor());
            filledCircle.setFillColor(background.getCurrentColor());

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
