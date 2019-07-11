package hr.fer.zemris.java.hw17.jvdraw.color;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Models a {@link JLabel} which keeps track of two {@link IColorProvider} and prints their currently selected colors.
 *
 * @author Marko Lazarić
 */
public class ColorLabel extends JLabel implements ColorChangeListener {

    /**
     * The color provider for the foreground.
     */
    private final IColorProvider foreground;

    /**
     * The color provider for the background.
     */
    private final IColorProvider background;

    /**
     * Creates a new {@link ColorLabel} with the given arguments.
     *
     * @param foreground the color provider for the foreground
     * @param background the color provider for the background
     *
     * @throws NullPointerException if either argument is null
     */
    public ColorLabel(IColorProvider foreground, IColorProvider background) {
        this.foreground = Objects.requireNonNull(foreground, "Foreground cannot be null.");
        this.background = Objects.requireNonNull(background, "Background cannot be null.");

        this.foreground.addColorChangeListener(this);
        this.background.addColorChangeListener(this);

        updateText();
    }

    @Override
    public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
        updateText();
    }

    /**
     * Updates the text of the label to show the currently selected foreground and background colors.
     */
    private void updateText() {
        setText("Foreground color: " + formatColorToString(foreground.getCurrentColor()) +
                ", background color: " + formatColorToString(background.getCurrentColor()) + ".");
    }

    /**
     * Helper function to format the {@link Color} as a string tuple.
     */
    private String formatColorToString(Color color) {
        return "(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
    }

}
