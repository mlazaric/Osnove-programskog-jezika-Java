package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.*;

/**
 * Models a listener which should be notified after a color change has occurred.
 *
 * @author Marko Lazarić
 */
public interface ColorChangeListener {

    /**
     * Called after a color change has occurred.
     *
     * @param source the source of the color change
     * @param oldColor the old selected color
     * @param newColor the new selected color
     */
    void newColorSelected(IColorProvider source, Color oldColor, Color newColor);

}