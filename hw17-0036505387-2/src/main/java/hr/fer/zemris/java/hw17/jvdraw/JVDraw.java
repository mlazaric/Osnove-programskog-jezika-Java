package hr.fer.zemris.java.hw17.jvdraw;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorArea;
import hr.fer.zemris.java.hw17.jvdraw.color.ColorLabel;

import javax.swing.*;
import java.awt.*;

public class JVDraw extends JFrame {

    private final ColorArea foreground;
    private final ColorArea background;

    private final ColorLabel colorLabel;

    public JVDraw() {
        setTitle("JVDraw");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        foreground = new ColorArea(Color.RED);
        background = new ColorArea(Color.BLUE);

        colorLabel = new ColorLabel(foreground, background);

        JToolBar toolBar = new JToolBar();

        toolBar.add(foreground);
        toolBar.add(background);

        add(toolBar, BorderLayout.PAGE_START);
        add(colorLabel, BorderLayout.PAGE_END);

        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JVDraw());
    }

}
