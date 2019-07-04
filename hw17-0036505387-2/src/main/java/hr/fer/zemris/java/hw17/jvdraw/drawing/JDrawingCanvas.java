package hr.fer.zemris.java.hw17.jvdraw.drawing;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorChangeListener;
import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tool.Tool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectPainter;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.function.Supplier;

public class JDrawingCanvas extends JComponent implements DrawingModelListener, ColorChangeListener {

    private final DrawingModel model;
    private final Supplier<Tool> currentTool;
    private final IColorProvider background;

    public JDrawingCanvas(DrawingModel model, Supplier<Tool> currentTool, IColorProvider background) {
        this.model = Objects.requireNonNull(model, "Drawing model cannot be null.");
        this.currentTool = Objects.requireNonNull(currentTool, "Current tool cannot be null.");
        this.background = Objects.requireNonNull(background, "Background cannot be null.");

        this.model.addDrawingModelListener(this);
        this.background.addColorChangeListener(this);
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
        g.setColor(background.getCurrentColor());
        g.fillRect(0, 0, getWidth(), getHeight());

        GeometricalObjectPainter painter = new GeometricalObjectPainter((Graphics2D) g);

        model.iterator().forEachRemaining(o -> o.accept(painter));
        currentTool.get().paint((Graphics2D) g);
    }

}
