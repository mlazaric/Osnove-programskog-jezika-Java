package hr.fer.zemris.java.hw17.jvdraw.drawing;

import hr.fer.zemris.java.hw17.jvdraw.tools.Tool;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.function.Supplier;

public class JDrawingCanvas extends JComponent implements DrawingModelListener {

    private final DrawingModel model;
    private final Supplier<Tool> currentTool;

    public JDrawingCanvas(DrawingModel model, Supplier<Tool> currentTool) {
        this.model = Objects.requireNonNull(model, "Drawing model cannot be null.");
        this.currentTool = Objects.requireNonNull(currentTool, "Current tool cannot be null.");

        this.model.addDrawingModelListener(this);
    }

    @Override
    public void objectsAdded(DrawingModel source, int firstIndex, int lastIndex) {
        repaint();
        currentTool.get().paint((Graphics2D) getGraphics());
    }

    @Override
    public void objectsRemoved(DrawingModel source, int firstIndex, int lastIndex) {
        repaint();
        currentTool.get().paint((Graphics2D) getGraphics());
    }

    @Override
    public void objectsChanged(DrawingModel source, int firstIndex, int lastIndex) {
        repaint();
        currentTool.get().paint((Graphics2D) getGraphics());
    }
}
