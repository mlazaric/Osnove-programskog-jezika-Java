package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Demonstrates the usage of {@link BarChartComponent}.
 *
 * @author Marko Lazarić
 */
@SuppressWarnings("serial")
public class BarChartDemo extends JFrame {

    /**
     * Creates a new {@link BarChartDemo} with the given argument.
     *
     * @param filepath the path to the file being shown
     * @param model the bar chart model
     *
     * @throws NullPointerException if either argument is null
     */
    public BarChartDemo(String filepath, BarChart model) {
        Objects.requireNonNull(filepath, "File path cannot be null.");
        Objects.requireNonNull(model, "Model cannot be null.");

        add(new JLabel(filepath, SwingConstants.CENTER), BorderLayout.PAGE_START);
        add(new BarChartComponent(model), BorderLayout.CENTER);

        setSize(500, 500);
        setTitle(filepath);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * Reads a bar chart from the file and shows it in a GUI.
     *
     * @param args the one and only argument is a path to the file to render as a bar chart
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Program expects exactly one argument.");
            System.exit(1);
        }

        BarChart model = readBarChartFromFile(args[0]);

        SwingUtilities.invokeLater(() -> {
            new BarChartDemo(args[0], model).setVisible(true);
        });
    }

    /**
     * Parses a {@link BarChart} from the given file.
     *
     * @param filepath the file to parse as {@link BarChart}
     * @return the parsed {@link BarChart}
     */
    private static BarChart readBarChartFromFile(String filepath) {
        List<String> lines = null;

        try {
            Path path = Paths.get(filepath);
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            System.out.println("Could not read from file '" + filepath + "'.");
            System.exit(1);
        }

        if (lines.size() < 6) {
            System.out.println("File does not contain enough lines.");
            System.exit(1);
        }

        String descriptionX = lines.get(0);
        String descriptionY = lines.get(1);
        List<XYValue> values = parseXYValues(lines.get(2));
        int minY = parseIntOrExit(lines.get(3));
        int maxY = parseIntOrExit(lines.get(4));
        int stepY = parseIntOrExit(lines.get(5));

        BarChart model = null;

        try {
            model = new BarChart(values, descriptionX, descriptionY, minY, maxY, stepY);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return model;
    }

    /**
     * Parses the {@link XYValue}s from a string.
     *
     * @param toParse the string to parse
     * @return a list of the parsed {@link XYValue}s
     */
    private static List<XYValue> parseXYValues(String toParse) {
        String[] xyValues = toParse.split(" ");
        List<XYValue> values = new ArrayList<>();

        for (String xyValue : xyValues) {
            String[] xAndY = xyValue.split(",");

            if (xAndY.length != 2) {
                System.out.println("'" + xyValue + "' should contain exactly one comma.");
                System.exit(1);
            }

            values.add(new XYValue(parseIntOrExit(xAndY[0]),
                                   parseIntOrExit(xAndY[1])));
        }

        return values;
    }

    /**
     * Parses an integer from the string or exits with an error message.
     *
     * @param toParse the string to parse as an integer
     * @return the parsed integer
     */
    private static int parseIntOrExit(String toParse) {
        try {
            return Integer.parseInt(toParse);
        }
        catch (NumberFormatException e) {
            System.out.println("'" + toParse + "' is not a parsable integer.");
            System.exit(1);
        }

        return -1; // This will never be reached.
    }

}
