package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.*;

public interface IColorProvider {

    Color getCurrentColor();

    void addColorChangeListener(ColorChangeListener l);

    void removeColorChangeListener(ColorChangeListener l);

}