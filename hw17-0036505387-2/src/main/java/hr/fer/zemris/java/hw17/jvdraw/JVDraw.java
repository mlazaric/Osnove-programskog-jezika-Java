package hr.fer.zemris.java.hw17.jvdraw;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorArea;
import hr.fer.zemris.java.hw17.jvdraw.color.ColorLabel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingObjectListModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JVDrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.editor.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tool.CircleTool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tool.FilledCircleTool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tool.LineTool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.tool.Tool;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor.GeometricalObjectWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple vector graphics editor implemented in Swing.
 *
 * @author Marko Lazarić
 */
public class JVDraw extends JFrame {

    /**
     * The color area for the foreground.
     */
    private ColorArea foreground;

    /**
     * The color area for the background.
     */
    private ColorArea background;

    /**
     * The canvas to use to paint the {@link GeometricalObject}s on.
     */
    private JDrawingCanvas canvas;

    /**
     * The model used to stored the drawn {@link GeometricalObject}.
     */
    private DrawingModel model;

    /**
     * The currently selected tool.
     */
    private Tool currentTool;

    /**
     * The currently opened file.
     */
    private Path currentFile;

    /**
     * Creates a new {@link JVDraw}.
     */
    public JVDraw() {
        setTitle("JVDraw");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
        setSize(750, 750);

        initGUI();

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }

        });
    }

    /**
     * Initialises the GUI of the app.
     */
    private void initGUI() {
        // Tool bar
        foreground = new ColorArea(Color.BLACK);
        background = new ColorArea(Color.WHITE);

        JRadioButton lineButton = new JRadioButton("Line");
        JRadioButton circleButton = new JRadioButton("Circle");
        JRadioButton filledCircleButton = new JRadioButton("Filled circle");

        ButtonGroup toolButtonGroup = new ButtonGroup();

        lineButton.setSelected(true);

        toolButtonGroup.add(lineButton);
        toolButtonGroup.add(circleButton);
        toolButtonGroup.add(filledCircleButton);

        JToolBar toolBar = new JToolBar();

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
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
        model = new JVDrawingModel();
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
                    GeometricalObject object = listOfObjects.getSelectedValue();

                    model.changeOrder(object, -1);
                    listOfObjects.setSelectedValue(object, true);
                }
                else if (e.getKeyChar() == '-' && listOfObjects.getSelectedIndex() < model.getSize() - 1) {
                    GeometricalObject object = listOfObjects.getSelectedValue();

                    model.changeOrder(object, 1);
                    listOfObjects.setSelectedValue(object, true);
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
                        catch (RuntimeException exc) {
                            JOptionPane.showMessageDialog(JVDraw.this, exc.getMessage());
                        }
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

        // Menu bar
        JMenuBar bar = createMenuBar();

        setJMenuBar(bar);
    }

    /**
     * Creates the menu bar.
     */
    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");

        file.add(new AbstractAction("Open") {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(".");

                jfc.setFileFilter(new FileNameExtensionFilter("JVD Files", "jvd"));

                if (jfc.showOpenDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
                    if (Files.isReadable(jfc.getSelectedFile().toPath())) {
                        model.clear();

                        parseFile(jfc.getSelectedFile().toPath());

                        currentFile = jfc.getSelectedFile().toPath();
                        model.clearModifiedFlag();
                    }
                }
            }

        });

        file.add(new AbstractAction("Save") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentFile == null) {
                    saveAs();
                }
                else {
                    save();
                }
            }

        });

        file.add(new AbstractAction("Save As") {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }

        });

        file.add(new AbstractAction("Export") {

            @Override
            public void actionPerformed(ActionEvent e) {
                export();
            }

        });

        file.add(new AbstractAction("Exit") {

            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }

        });

        bar.add(file);

        return bar;
    }

    /**
     * Checks whether the model has been modified.
     *
     * If it has, it asks the user whether they want to save the file.
     * Otherwise, it just closes the window.
     */
    private void exit() {
        if (model.isModified()) {
            int result = JOptionPane.showConfirmDialog(this, "You have unsaved changes. " +
                    "Do you want to save them before exiting?", "Unsaved changes.", JOptionPane.YES_NO_CANCEL_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                if (currentFile == null) {
                    saveAs();
                }
                else {
                    save();
                }
            }
            else if (result == JOptionPane.NO_OPTION) {
                dispose();
            }
        }
        else {
            dispose();
        }
    }

    /**
     * Exports the currently drawn {@link GeometricalObject}s as a PNG/JPG/GIF.
     */
    private void export() {
        JFileChooser jfc = new JFileChooser(".");

        jfc.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif"));

        if (jfc.showSaveDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
            Path imagePath = jfc.getSelectedFile().toPath();
            String[] parts = imagePath.toString().split("\\.");
            String extension = parts[parts.length - 1];

            if (extension.equals("jpg") || extension.equals("png") || extension.equals("gif")) {
                GeometricalObjectBBCalculator boundingBoxCalc = new GeometricalObjectBBCalculator();

                for (GeometricalObject object : model) {
                    object.accept(boundingBoxCalc);
                }

                Rectangle boundingBox = boundingBoxCalc.getBoundingBox();

                BufferedImage image = new BufferedImage(
                        boundingBox.width, boundingBox.height, BufferedImage.TYPE_3BYTE_BGR
                );
                Graphics2D g = image.createGraphics();

                g.translate(-boundingBox.x, -boundingBox.y);

                g.setColor(background.getCurrentColor());
                g.fillRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);

                GeometricalObjectPainter painter = new GeometricalObjectPainter(g);

                for (GeometricalObject object : model) {
                    object.accept(painter);
                }

                g.dispose();

                try {
                    ImageIO.write(image, extension, imagePath.toFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Saves the model to the currently opened file.
     */
    private void save() {
        model.clearModifiedFlag();

        try (Writer writer = Files.newBufferedWriter(currentFile)) {
            GeometricalObjectWriter visitor = new GeometricalObjectWriter(writer);

            for (GeometricalObject object : model) {
                object.accept(visitor);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Asks the user where to save the file and then saves the model to that file.
     */
    private void saveAs() {
        JFileChooser jfc = new JFileChooser(".");

        jfc.setFileFilter(new FileNameExtensionFilter("JVD Files", "jvd"));

        if (jfc.showSaveDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
            currentFile = jfc.getSelectedFile().toPath();

            save();
        }
    }

    /**
     * Sets the current tool to the given argument.
     */
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

    /**
     * Returns the currently selected tool.
     */
    private Tool getCurrentTool() {
        return currentTool;
    }

    /**
     * The regular expression used for parsing a {@link Line}.
     */
    private static final Pattern LINE_REGEX = Pattern.compile("^LINE (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+)$");

    /**
     * The regular expression used for parsing a {@link Circle}.
     */
    private static final Pattern CIRCLE_REGEX = Pattern.compile("^CIRCLE (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+)$");

    /**
     * The regular expression used for parsing a {@link FilledCircle}.
     */
    private static final Pattern FILLED_CIRCLE_REGEX = Pattern.compile("^FCIRCLE (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+)$");

    /**
     * Parses a JVD file and adds the parsed {@link GeometricalObject}s to the model.
     *
     * @param file the file to parse
     */
    private void parseFile(Path file) {
        try {
            Files.lines(file, StandardCharsets.UTF_8)
                    .filter(l -> !l.isEmpty())
                    .map(this::parseLine)
                    .forEach(model::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses a line and returns the parsed {@link GeometricalObject}.
     *
     * @param line the line to parse
     * @return the parsed {@link GeometricalObject}
     *
     * @throws RuntimeException if the line is not a parsable {@link GeometricalObject}
     */
    private GeometricalObject parseLine(String line) {
        Matcher matcher = LINE_REGEX.matcher(line);

        if (matcher.matches()) {
            return new Line(
                    new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                    new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))),
                    new Color(Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)), Integer.parseInt(matcher.group(7)))
            );
        }

        matcher = CIRCLE_REGEX.matcher(line);

        if (matcher.matches()) {
            return new Circle(
                    new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                    Integer.parseInt(matcher.group(3)),
                    new Color(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)))
            );
        }

        matcher = FILLED_CIRCLE_REGEX.matcher(line);

        if (matcher.matches()) {
            return new FilledCircle(
                    new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                    Integer.parseInt(matcher.group(3)),
                    new Color(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6))),
                    new Color(Integer.parseInt(matcher.group(7)), Integer.parseInt(matcher.group(8)), Integer.parseInt(matcher.group(9)))
            );
        }

        throw new RuntimeException("'" + line + "' is not parsable.");
    }

    /**
     * Starts the {@link JVDraw} GUI.
     *
     * @param args the arguments are ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JVDraw());
    }

}
