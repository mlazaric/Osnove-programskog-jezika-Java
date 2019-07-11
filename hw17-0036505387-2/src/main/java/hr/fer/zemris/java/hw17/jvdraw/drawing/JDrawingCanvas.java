package hr.fer.zemris.java.hw17.jvdraw.drawing;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorChangeListener;
import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tool.Tool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectPainter;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A {@link JComponent} which draws all the {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject}s within
 * the {@link DrawingModel}.
 *
 * @author Marko Lazarić
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener, ColorChangeListener {

    /**
     * The drawing model containing the {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject}s.
     */
    private final DrawingModel model;

    /**
     * A provider for the current tool.
     */
    private final Supplier<Tool> currentTool;

    /**
     * Creates a new {@link JDrawingCanvas} with the given arguments.
     *
     * @param model the drawing model containing the {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject}s
     * @param currentTool a provider for the current tool
     *
     * @throws NullPointerException if any of the arguments is null
     */
    public JDrawingCanvas(DrawingModel model, Supplier<Tool> currentTool) {
        this.model = Objects.requireNonNull(model, "Drawing model cannot be null.");
        this.currentTool = Objects.requireNonNull(currentTool, "Current tool cannot be null.");

        this.model.addDrawingModelListener(this);
    }

    @Override
    public void objectsAdded(DrawingModel source, int firstIndex, int lastIndex) {
        repaint();
    }

    @Override
    public void objectsRemoved(DrawingModel source, int firstIndex, int lastIndex) {
        repaint();
    }

    @Override
    public void objectsChanged(DrawingModel source, int firstIndex, int lastIndex) {
        repaint();
    }

    @Override
    public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        GeometricalObjectPainter painter = new GeometricalObjectPainter((Graphics2D) g);

        model.iterator().forEachRemaining(o -> o.accept(painter));
        currentTool.get().paint((Graphics2D) g);
    }

}
