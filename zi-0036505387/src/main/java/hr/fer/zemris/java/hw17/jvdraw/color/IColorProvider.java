package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.*;

/**
 * Models an object which can provide a {@link Color}.
 *
 * @author Marko Lazarić
 */
public interface IColorProvider {

    /**
     * Returns the currently selected color.
     *
     * @return the currently selected color
     */
    Color getCurrentColor();

    /**
     * Adds a color change listener.
     *
     * @param l the listener to add
     */
    void addColorChangeListener(ColorChangeListener l);

    /**
     * Removes the color change listener.
     *
     * @param l the listener to remove
     */
    void removeColorChangeListener(ColorChangeListener l);

}