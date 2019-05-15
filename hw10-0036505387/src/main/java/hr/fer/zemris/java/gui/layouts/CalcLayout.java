package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Models a specific layout used for {@link hr.fer.zemris.java.gui.calc.Calculator}.
 *
 * @author Marko Lazarić
 */
public class CalcLayout implements LayoutManager2 {

    /**
     * The mapping of positions to their components.
     */
    private final Map<RCPosition, Component> components;

    /**
     * The margin between rows and columns.
     */
    private final int margin;

    /**
     * The number of rows.
     */
    private final static int NUMBER_OF_ROWS = 5;

    /**
     * The number of columns.
     */
    private final static int NUMBER_OF_COLUMNS = 7;

    /**
     * Default element height. Used if all elements return null.
     */
    private static final int DEFAULT_ELEMENT_HEIGHT = 10;

    /**
     * Default element width. Used if all elements return null.
     */
    private static final int DEFAULT_ELEMENT_WIDTH = 10;

    /**
     * Creates a {@link CalcLayout} with the given argument.
     *
     * @param margin the margin between rows and columns
     */
    public CalcLayout(int margin) {
        if (margin < 0) {
            throw new CalcLayoutException("Cannot use negative margin.");
        }

        this.margin = margin;
        this.components = new HashMap<>();
    }

    /**
     * Creates a {@link CalcLayout} with no margin.
     */
    public CalcLayout() {
        this(0);
    }

    /**
     * Checks whether the position is valid.
     *
     * @param position the position to check
     *
     * @throws CalcLayoutException if the position is not valid
     */
    private void checkRCPosition(RCPosition position) {
        if (position.getRow() == 1 && position.getColumn() > 1 && position.getColumn() < 6) {
            throw new CalcLayoutException("Invalid position " + position + ", 1,1 spans five spaces.");
        }

        if (position.getRow() < 1 || position.getRow() > NUMBER_OF_ROWS)
        {
            throw new CalcLayoutException("Invalid position " + position + " row out of range.");
        }

        if (position.getColumn() < 1 || position.getColumn() > NUMBER_OF_COLUMNS)
        {
            throw new CalcLayoutException("Invalid position " + position + " column out of range.");
        }
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof RCPosition) {
            RCPosition position = (RCPosition) constraints;

            checkRCPosition(position);

            if (components.containsKey(position)) {
                throw new CalcLayoutException("Cannot replace component with constraint: " + position);
            }

            components.put(position, comp);
        }
        else if (constraints instanceof String) {
            String stringPosition = (String) constraints;

            try {
                String[] parts = stringPosition.split(",");

                if (parts.length != 2) {
                    throw new CalcLayoutException(stringPosition + " is not a valid RCPosition, contains too few or too many commas.");
                }

                addLayoutComponent(comp, new RCPosition(Integer.parseInt(parts[0]),
                                                        Integer.parseInt(parts[1])));
            }
            catch (NumberFormatException e) {
                throw new CalcLayoutException(stringPosition + " is not a valid RCPosition, could not parse it as a pair of numbers.");
            }
        }
        else {
            throw new UnsupportedOperationException(constraints + " is not an instance of RCPosition.");
        }
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        components.values().remove(comp);
    }

    @Override
    public Dimension maximumLayoutSize(Container parent) {
        return layoutSize(parent, Component::getMaximumSize);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return layoutSize(parent, Component::getPreferredSize);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return layoutSize(parent, Component::getMinimumSize);
    }

    /**
     * Helper method to calculate the maximum/preferred/minimum layout size.
     *
     * @param parent the parent container
     * @param extractionFunction the function that extracts Dimension from Component
     * @return the layout size calculated using the extraction function
     */
    private Dimension layoutSize(Container parent, Function<Component, Dimension> extractionFunction) {
        int height = components.values()
                               .stream()
                               .map(extractionFunction)         // Extract dimension
                               .filter(Objects::nonNull)        // Remove nulls
                               .mapToInt(d -> d.height)         // Map to height
                               .max()                           // Get max of all the heights
                               .orElse(DEFAULT_ELEMENT_HEIGHT); // If they're all null, use the default value

        int width = components.entrySet()
                              .stream()
                              .filter(e -> extractionFunction.apply(e.getValue()) != null)      // Remove nulls
                              .mapToInt(entry -> {
                                    RCPosition position = entry.getKey();
                                    Component component = entry.getValue();
                                    Dimension dimension = extractionFunction.apply(component);

                                    if (position.getColumn() == 1 && position.getRow() == 1) {  // Element at (1, 1) takes up 5 widths + 4 margins
                                        return (dimension.width - 4 * margin) / 5;
                                    }

                                    return dimension.width;
                              })
                              .max()
                              .orElse(DEFAULT_ELEMENT_WIDTH);                                   // If they're all null, use the default value

        // Multiply by number of elements in the row/column
        height *= NUMBER_OF_ROWS;
        width *= NUMBER_OF_COLUMNS;

        // Add margins
        height += margin * (NUMBER_OF_ROWS - 1);
        width += margin * (NUMBER_OF_COLUMNS - 1);

        Insets insets = parent.getInsets();

        // Add parent insets
        height += insets.bottom + insets.top;
        width += insets.left + insets.right;

        return new Dimension(width, height);
    }

    @Override
    public void layoutContainer(Container parent) {
        ElementPositioner positioner = new ElementPositioner(parent, margin);

        for (Map.Entry<RCPosition, Component> entry : components.entrySet()) {
            RCPosition position = entry.getKey();
            Component component = entry.getValue();

            component.setBounds(positioner.getBounds(position));
        }
    }

    /**
     * Helper class for positioning elements.
     * Mostly needed to uniformly distribute the left over pixels.
     *
     * @author Marko Lazarić
     */
    private static class ElementPositioner {

        /**
         * The heights of the rows.
         */
        private final int[] heights;

        /**
         * The widths of the columns.
         */
        private final int[] widths;

        /**
         * The parent container.
         */
        private final Container parent;

        /**
         * The margin between rows and columns.
         */
        private final int margin;

        /**
         * Creates a new {@link ElementPositioner} with the given arguments.
         *
         * @param parent the parent container
         * @param margin the margin between rows and columns
         */
        private ElementPositioner(Container parent, int margin) {
            this.parent = parent;
            this.margin = margin;
            this.widths = calculateColumnWidths();
            this.heights = calculateRowHeights();
        }

        /**
         * Creates an array with {@code numberOfElements} of {@code size} and distributes the {@code leftOver} uniformly.
         *
         * @param size the size of every element
         * @param leftOver the left over from integer divison
         * @param numberOfElements the number of elements
         * @return an array of sizes for each element with {@code leftOver} distributed uniformly
         */
        private int[] calculateSize(int size, int leftOver, int numberOfElements) {
            int[] elementSizes = new int[numberOfElements];

            Arrays.fill(elementSizes, size);

            if (leftOver <= 0) {
                return elementSizes;
            }

            // Uniformly distribute the left over pixels
            int step = numberOfElements / leftOver;

            for (int index = 0; index < elementSizes.length; index += step) {
                ++elementSizes[index];
                --leftOver;

                if (leftOver == 0) {
                    break;
                }
            }

            return elementSizes;
        }

        /**
         * Calculates the column widths.
         *
         * @return the column widths
         */
        private int[] calculateColumnWidths() {
            Insets insets = parent.getInsets();

            // We need to divide parent's width (without insets) into columns
            int parentWidth = parent.getWidth() - insets.left - insets.right;

            // First remove all margins
            int parentWidthWithoutMargins = parentWidth - (NUMBER_OF_COLUMNS - 1) * margin;

            // Divide parent width without margins equally to all elements
            int elementWidth = parentWidthWithoutMargins / NUMBER_OF_COLUMNS;

            // Left over width because of integer division which we need to distribute uniformly
            int leftOverWidth = parentWidthWithoutMargins % NUMBER_OF_COLUMNS;

            return calculateSize(elementWidth, leftOverWidth, NUMBER_OF_COLUMNS);
        }

        /**
         * Calculates the row heights.
         *
         * @return the row heights
         */
        private int[] calculateRowHeights() {
            Insets insets = parent.getInsets();

            // We need to divide parent's height (without insets) into rows
            int parentHeight = parent.getHeight() - insets.top - insets.bottom;

            // First remove all margins
            int parentHeightWithoutMargins = parentHeight - (NUMBER_OF_ROWS - 1) * margin;

            // Divide parent height without margins equally to all elements
            int elementHeight = parentHeightWithoutMargins / NUMBER_OF_ROWS;

            // Left over height because of integer division which we need to distribute uniformly
            int leftOverHeight = parentHeightWithoutMargins % NUMBER_OF_ROWS;

            return calculateSize(elementHeight, leftOverHeight, NUMBER_OF_ROWS);
        }

        /**
         * Calculates the absolute position of the element at the specified relative position.
         *
         * @param position the relative position of the element
         * @return the absolute position of the element at the specified relative position
         */
        private Point calculatePositionOfElement(RCPosition position) {
            Insets insets = parent.getInsets();

            int width = insets.top;
            int height = insets.left;

            if (position.getColumn() > 1) {
                for (int col = 0; col < position.getColumn() - 1; ++col) {
                    width += widths[col] + margin;
                }
            }

            if (position.getRow() > 1) {
                for (int row = 0; row < position.getRow() - 1; ++row) {
                    height += heights[row] + margin;
                }
            }

            return new Point(width, height);
        }

        /**
         * Calculates the bounds of the element at the specified relative position.
         *
         * @param position the relative position of the element
         * @return the bounds of the element at the specified relative position
         */
        private Rectangle getBounds(RCPosition position) {
            if (position.getRow() == 1 && position.getColumn() == 1) {
                Point elementPosition = calculatePositionOfElement(position);
                Point positionOf1_6 = calculatePositionOfElement(new RCPosition(1, 6));

                // Element (1, 1) spans from top, left corner to the position of (1, 6) - margin
                return new Rectangle(elementPosition.x, elementPosition.y, positionOf1_6.x - margin, heights[position.getRow() - 1]);
            }

            Point elementPosition = calculatePositionOfElement(position);

            return new Rectangle(elementPosition.x, elementPosition.y, widths[position.getColumn() - 1], heights[position.getRow() - 1]);
        }
    }
}
