package hr.fer.zemris.java.hw17.jvdraw;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorArea;
import hr.fer.zemris.java.hw17.jvdraw.color.ColorLabel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingObjectListModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JVDrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tools.CircleTool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tools.FilledCircleTool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tools.Tool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tools.LineTool;

import javax.swing.*;
import java.awt.*;

public class JVDraw extends JFrame {

    private JDrawingCanvas canvas;
    private Tool currentTool;

    public JVDraw() {
        setTitle("JVDraw");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setSize(500, 500);

        initGUI();
    }

    private void initGUI() {
        // Tool bar
        ColorArea foreground = new ColorArea(Color.RED);
        ColorArea background = new ColorArea(Color.BLUE);

        JRadioButton lineButton = new JRadioButton("Line");
        JRadioButton circleButton = new JRadioButton("Circle");
        JRadioButton filledCircleButton = new JRadioButton("Filled circle");

        ButtonGroup toolButtonGroup = new ButtonGroup();

        lineButton.setSelected(true);

        toolButtonGroup.add(lineButton);
        toolButtonGroup.add(circleButton);
        toolButtonGroup.add(filledCircleButton);

        JToolBar toolBar = new JToolBar();

        toolBar.add(foreground);
        toolBar.add(background);
        toolBar.add(lineButton);
        toolBar.add(circleButton);
        toolBar.add(filledCircleButton);

        add(toolBar, BorderLayout.PAGE_START);

        // Color label
        ColorLabel colorLabel = new ColorLabel(foreground, background);

        add(colorLabel, BorderLayout.PAGE_END);

        // List of geometrical objects
        DrawingModel model = new JVDrawingModel();
        JList<GeometricalObject> listOfObjects = new JList<>(new DrawingObjectListModel(model));

        add(new JScrollPane(listOfObjects), BorderLayout.LINE_END);

        // Drawing canvas
        canvas = new JDrawingCanvas(model, this::getCurrentTool, background);
        setCurrentTool(new LineTool(model, foreground, background, canvas));

        add(canvas, BorderLayout.CENTER);

        // Tool action listeners
        lineButton.addActionListener(a -> setCurrentTool(new LineTool(model, foreground, background, canvas)));
        circleButton.addActionListener(a -> setCurrentTool(new CircleTool(model, foreground, background, canvas)));
        filledCircleButton.addActionListener(a -> setCurrentTool(new FilledCircleTool(model, foreground, background, canvas)));
    }

    private void setCurrentTool(Tool currentTool) {
        // If the user clicked the same tool again, there is no need to set it
        if (this.currentTool != null && this.currentTool.getClass().equals(currentTool.getClass())) {
            return;
        }

        // Remove old mouse listener and add the new one
        canvas.removeMouseListener(this.currentTool);
        canvas.removeMouseMotionListener(this.currentTool);
        canvas.addMouseListener(currentTool);
        canvas.addMouseMotionListener(currentTool);

        this.currentTool = currentTool;
    }

    private Tool getCurrentTool() {
        return currentTool;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JVDraw());
    }

}
