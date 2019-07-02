package hr.fer.zemris.java.hw17.jvdraw.color;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ColorLabel extends JLabel implements ColorChangeListener {

    private final IColorProvider foreground;
    private final IColorProvider background;

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

    private void updateText() {
        setText("Foreground color: " + formatColorToString(foreground.getCurrentColor()) +
                ", background color: " + formatColorToString(background.getCurrentColor()) + ".");
    }

    private String formatColorToString(Color color) {
        return "(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
    }

}
