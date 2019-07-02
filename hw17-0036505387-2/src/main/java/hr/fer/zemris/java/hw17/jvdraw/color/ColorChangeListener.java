package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.*;

public interface ColorChangeListener {

    void newColorSelected(IColorProvider source, Color oldColor, Color newColor);

}