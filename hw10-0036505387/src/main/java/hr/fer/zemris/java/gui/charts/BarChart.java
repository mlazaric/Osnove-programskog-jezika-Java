package hr.fer.zemris.java.gui.charts;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Models a BarChart represented by a list of values, descriptions of the values, the minimum, the maximum and the step
 * values for the y.
 *
 * @author Marko Lazarić
 */
public class BarChart {

    /**
     * The bars in this bar chart.
     */
    private final List<XYValue> values;

    /**
     * The description of the x values.
     */
    private final String descriptionX;

    /**
     * The description of the y values.
     */
    private final String descriptionY;

    /**
     * The minimum y value.
     */
    private final int minY;

    /**
     * The maximum y value.
     */
    private final int maxY;

    /**
     * The step of y values.
     */
    private final int stepY;

    /**
     * Creates a new {@link BarChart} with the given arguments.
     *
     * @param values the bars in this bar chart
     * @param descriptionX the description of the x values
     * @param descriptionY the description of the y values
     * @param minY the minimum y value
     * @param maxY the maximum y value
     * @param stepY the step of y values
     *
     * @throws NullPointerException if {@code values} is {@code null}
     * @throws IllegalArgumentException if {@code minY} is negative
     * @throws IllegalArgumentException if {@code minY} is greater than or equal to {@code maxY}
     * @throws IllegalArgumentException if {@code values} is empty
     * @throws IllegalArgumentException if {@code step} is less than 1
     * @throws IllegalArgumentException if {@code minY} is greater than one of the y values in the list
     */
    public BarChart(List<XYValue> values, String descriptionX, String descriptionY, int minY, int maxY, int stepY) {
        this.values = Objects.requireNonNull(values, "Cannot create BarChart for null.");
        this.descriptionX = (descriptionX == null) ? "" : descriptionX; // No need to throw a NPE in this case.
        this.descriptionY = (descriptionY == null) ? "" : descriptionY; // Just set it to an empty string.
        this.minY = minY;
        this.maxY = maxY;
        this.stepY = stepY;

        if (minY < 0) {
            throw new IllegalArgumentException("minY cannot be negative.");
        }
        if (maxY <= minY) {
            throw new IllegalArgumentException("maxY must be greater than minY.");
        }
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Cannot create bar chart of empty values.");
        }
        if (stepY < 1) {
            throw new IllegalArgumentException("Step of y label cannot be less than 1.");
        }

        int calculatedMinY = values.stream()
                                   .mapToInt(XYValue::getY)
                                   .min()
                                   .orElse(minY);

        if (calculatedMinY < minY) {
            throw new IllegalArgumentException("minY is not the minimum y of values.");
        }

        // Sort based on x values
        Collections.sort(this.values, Comparator.comparingInt(XYValue::getX));
    }

    /**
     * Returns the bars in this bar chart.
     *
     * @return the bars in this bar chart
     */
    public List<XYValue> getValues() {
        return values;
    }

    /**
     * Returns the description of the x values.
     * @return the description of the x values
     */
    public String getDescriptionX() {
        return descriptionX;
    }

    /**
     * Returns the description of the y values.
     * @return the description of the y values
     */
    public String getDescriptionY() {
        return descriptionY;
    }

    /**
     * Returns the minimum y value.
     *
     * @return the minimum y value
     */
    public int getMinY() {
        return minY;
    }

    /**
     * Returns the maximum y value.
     *
     * @return the maximum y value
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * Returns the step of y values.
     *
     * @return the step of y values
     */
    public int getStepY() {
        return stepY;
    }
}
