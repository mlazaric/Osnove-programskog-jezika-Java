package hr.fer.zemris.java.gui.charts;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BarChart {

    private final List<XYValue> values;
    private final String descriptionX;
    private final String descriptionY;
    private final int minY;
    private final int maxY;
    private final int stepY;

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

    public List<XYValue> getValues() {
        return values;
    }

    public String getDescriptionX() {
        return descriptionX;
    }

    public String getDescriptionY() {
        return descriptionY;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getStepY() {
        return stepY;
    }
}
