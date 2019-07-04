package hr.fer.zemris.java.hw17.jvdraw;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorArea;
import hr.fer.zemris.java.hw17.jvdraw.color.ColorLabel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingObjectListModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JVDrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tool.CircleTool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tool.FilledCircleTool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tool.Tool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tool.LineTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        ColorArea foreground = new ColorArea(Color.BLACK);
        ColorArea background = new ColorArea(Color.WHITE);

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

        // List of geometrical object
        DrawingModel model = new JVDrawingModel();
        JList<GeometricalObject> listOfObjects = new JList<>(new DrawingObjectListModel(model));

        add(new JScrollPane(listOfObjects), BorderLayout.LINE_END);

        listOfObjects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listOfObjects.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    model.remove(listOfObjects.getSelectedValue());
                }
                else if (e.getKeyChar() == '+' && listOfObjects.getSelectedIndex() > 0) {
                    model.changeOrder(listOfObjects.getSelectedValue(), -1);
                }
                else if (e.getKeyChar() == '-' && listOfObjects.getSelectedIndex() < model.getSize() - 1) {
                    model.changeOrder(listOfObjects.getSelectedValue(), 1);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        listOfObjects.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    GeometricalObject object = listOfObjects.getSelectedValue();

                    if (object == null) {
                        return;
                    }

                    GeometricalObjectEditor editor = object.createGeometricalObjectEditor();

                    int option = JOptionPane.showConfirmDialog(JVDraw.this, editor, "Edit", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION) {
                        try {
                            editor.checkEditing();
                            editor.acceptEditing();
                            canvas.repaint();
                        }
                        catch (RuntimeException editingFailed) {}
                    }
                }
            }

        });

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
