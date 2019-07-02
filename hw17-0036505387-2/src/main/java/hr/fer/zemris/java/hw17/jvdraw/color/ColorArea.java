package hr.fer.zemris.java.hw17.jvdraw.color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColorArea extends JComponent implements IColorProvider, MouseListener {

    private static final Dimension PREFERRED_DIMENSION = new Dimension(15, 15);

    private Color selectedColor;
    private List<ColorChangeListener> listeners;

    public ColorArea(Color selectedColor) {
        this.selectedColor = Objects.requireNonNull(selectedColor, "Selected color cannot be null.");
        this.listeners = new ArrayList<>();
        this.addMouseListener(this);
    }

    public void setSelectedColor(Color selectedColor) {
        Color oldColor = this.selectedColor;

        this.selectedColor = selectedColor;

        fire(oldColor, selectedColor);
    }

    @Override
    public Dimension getPreferredSize() {
        return PREFERRED_DIMENSION;
    }

    @Override
    public Color getCurrentColor() {
        return selectedColor;
    }

    @Override
    public void addColorChangeListener(ColorChangeListener l) {
        listeners.add(l);
    }

    @Override
    public void removeColorChangeListener(ColorChangeListener l) {
        listeners.remove(l);
    }

    private void fire(Color oldColor, Color newColor) {
        listeners.stream()
                 .forEach(l -> l.newColorSelected(this, oldColor, newColor));

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(selectedColor);

        g.fillRect(0, 0, PREFERRED_DIMENSION.width, PREFERRED_DIMENSION.height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Color newColor = JColorChooser.showDialog(null, "Set new color", getCurrentColor());

        if (newColor != null) {
            setSelectedColor(newColor);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}
