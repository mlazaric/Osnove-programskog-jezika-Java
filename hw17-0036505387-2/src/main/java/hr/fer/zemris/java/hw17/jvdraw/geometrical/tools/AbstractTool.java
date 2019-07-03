package hr.fer.zemris.java.hw17.jvdraw.geometrical.tools;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;

import java.util.Objects;

public abstract class AbstractTool implements Tool {

    protected DrawingModel model;
    protected IColorProvider foreground;
    protected IColorProvider background;
    protected JDrawingCanvas canvas;

    public AbstractTool(DrawingModel model, IColorProvider foreground, IColorProvider background, JDrawingCanvas canvas) {
        this.model = Objects.requireNonNull(model, "Drawing model cannot be null.");
        this.foreground = Objects.requireNonNull(foreground, "Foreground color provider cannot be null.");
        this.background = Objects.requireNonNull(background, "Background color provider cannot be null.");
        this.canvas = Objects.requireNonNull(canvas, "Drawing canvas cannot be null.");
    }

}
