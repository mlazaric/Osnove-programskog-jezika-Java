package hr.fer.zemris.java.hw17.jvdraw.geometrical.tool;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;

import java.util.Objects;

/**
 * An abstract {@link Tool} which can be used for drawing {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject}s.
 *
 * @author Marko Lazarić
 */
public abstract class AbstractTool implements Tool {

    /**
     * The model to draw the {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} on.
     */
    protected DrawingModel model;

    /**
     * The color provider for the foreground.
     */
    protected IColorProvider foreground;

    /**
     * The color provider for the background.
     */
    protected IColorProvider background;

    /**
     * The canvas to draw the {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} with.
     */
    protected JDrawingCanvas canvas;

    /**
     * Creates a new {@link AbstractTool} with the given arguments.
     *
     * @param model the model to draw the {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} on
     * @param foreground the color provider for the foreground
     * @param background the color provider for the background
     * @param canvas the canvas to draw the {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject} with
     *
     * @throws NullPointerException if any argument is null
     */
    public AbstractTool(DrawingModel model, IColorProvider foreground, IColorProvider background, JDrawingCanvas canvas) {
        this.model = Objects.requireNonNull(model, "Drawing model cannot be null.");
        this.foreground = Objects.requireNonNull(foreground, "Foreground color provider cannot be null.");
        this.background = Objects.requireNonNull(background, "Background color provider cannot be null.");
        this.canvas = Objects.requireNonNull(canvas, "Drawing canvas cannot be null.");
    }

}
