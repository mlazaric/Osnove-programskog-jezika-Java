package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import static java.lang.Math.ceil;
import static java.lang.Math.max;

/**
 * Draws a bar chart modeled by {@link BarChart}.
 *
 * @author Marko Lazarić
 */
@SuppressWarnings("serial")
public class BarChartComponent extends JComponent {

    /**
     * The space between the description of the y values and the y labels.
     */
    private static final int SPACE_BETWEEN_Y_DESC_AND_Y_LABEL = 5;

    /**
     * The space between the description of the x values and the x labels.
     */
    private static final int SPACE_BETWEEN_X_DESC_AND_X_LABEL = 20;

    /**
     * The right margin.
     */
    private static final int RIGHT_MARGIN = 20;

    /**
     * The top margin.
     */
    private static final int TOP_MARGIN = 20;

    /**
     * The left margin.
     */
    private static final int LEFT_MARGIN = 30;

    /**
     * The bottom margin.
     */
    private static final int BOTTOM_MARGIN = 10;

    /**
     * The color of the lines and the text.
     */
    private static final Color LABEL_COLOR = Color.BLACK;

    /**
     * The border color of the bars.
     */
    private static final Color BAR_BORDER_COLOR = Color.decode("#006064");

    /**
     * The fill color of the bars.
     */
    private static final Color BAR_FILL_COLOR = Color.decode("#00BCD4");

    /**
     * The bar chart model.
     */
    private final BarChart barChart;

    /**
     * Creates a new {@link BarChartComponent} with the given argument.
     *
     * @param barChart the bar chart model
     *
     * @throws NullPointerException if {@code barChart} is {@code null}
     */
    public BarChartComponent(BarChart barChart) {
        this.barChart = Objects.requireNonNull(barChart, "Cannot use null bar chart.");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int widthLabelY = max(g.getFontMetrics().stringWidth(barChart.getMinY() + ""),
                              g.getFontMetrics().stringWidth(barChart.getMaxY() + ""));

        // Origin coordinates
        int originX = LEFT_MARGIN + g.getFontMetrics().getHeight() + SPACE_BETWEEN_Y_DESC_AND_Y_LABEL + widthLabelY;
        int originY = getHeight() - BOTTOM_MARGIN - g.getFontMetrics().getHeight() - SPACE_BETWEEN_X_DESC_AND_X_LABEL;

        // Widths and heights of the labels
        int widthOfXValue = (getWidth() - originX - RIGHT_MARGIN) / barChart.getValues().size();
        int numberOfYLabels = (int) ceil((barChart.getMaxY() - barChart.getMinY()) * 1.0 / barChart.getStepY());
        int heightOfYValue = (originY - TOP_MARGIN) / numberOfYLabels;

        // Draw everything
        drawBars(g, originX, originY, widthOfXValue, heightOfYValue);
        drawXLabels(g, originX, originY, widthOfXValue);
        drawYLabels(g, originX, originY, numberOfYLabels, heightOfYValue);
        drawDescriptionX(g, originX, originY);
        drawDescriptionY(g2, originY);
    }

    /**
     * Draws the bars of the bar chart.
     *
     * @param g the graphics object
     * @param originX the x coordinate of origin
     * @param originY the y coordinate of origin
     * @param widthOfXValue the width of the x values
     * @param heightOfYValue the height of the y step values
     */
    private void drawBars(Graphics g, int originX, int originY, int widthOfXValue, int heightOfYValue) {
        int index = 0;

        for (XYValue value : barChart.getValues()) {
            int x = originX + index * widthOfXValue;
            int width = widthOfXValue;

            int height = (int) (((double) (value.getY() - barChart.getMinY()) / barChart.getStepY()) * heightOfYValue);
            int y = originY - height;

            g.setColor(BAR_FILL_COLOR);
            g.fillRect(x, y, width, height);
            g.setColor(BAR_BORDER_COLOR);
            g.drawRect(x, y, width, height);
            g.setColor(LABEL_COLOR);

            ++index;
        }
    }

    /**
     * Draws the description of the y values.
     *
     * @param g2 the graphics2d object
     * @param originY the y coordinate of the origin
     */
    private void drawDescriptionY(Graphics2D g2, int originY) {
        AffineTransform defaultAt = g2.getTransform();
        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);

        g2.setTransform(at);

        String description = barChart.getDescriptionY();
        int heightOfDescription = g2.getFontMetrics().stringWidth(description);

        g2.drawString(description, -(originY / 2 + heightOfDescription / 2), RIGHT_MARGIN);

        g2.setTransform(defaultAt);
    }

    /**
     * Draws the description of the x values.
     *
     * @param g the graphics object
     * @param originX the x coordinate of origin
     * @param originY the y coordinate of origin
     */
    private void drawDescriptionX(Graphics g, int originX, int originY) {
        String description = barChart.getDescriptionX();
        int widthOfDescription = g.getFontMetrics().stringWidth(description);

        g.drawString(barChart.getDescriptionX(), (originX + getWidth()) / 2 - widthOfDescription / 2,
                originY + SPACE_BETWEEN_X_DESC_AND_X_LABEL + g.getFontMetrics().getHeight());
    }

    /**
     * Draws the y step labels.
     *
     * @param g the graphics object
     * @param originX the x coordinate of origin
     * @param originY the y coordinate of origin
     * @param numberOfYLabels the number of y step labels
     * @param heightOfYValue the height of the y step values
     */
    private void drawYLabels(Graphics g, int originX, int originY, int numberOfYLabels, int heightOfYValue) {
        for (int index = 0; index <= numberOfYLabels; ++index) {
            String yLabel = "" + (index * barChart.getStepY() + barChart.getMinY());
            int widthOfThisLabel = g.getFontMetrics().stringWidth(yLabel);

            g.drawLine(originX, originY - index * heightOfYValue, originX - 5, originY - index * heightOfYValue);
            g.drawString(yLabel, originX - widthOfThisLabel - 10, originY - index * heightOfYValue + g.getFontMetrics().getHeight() / 2 - 2);
        }

        g.drawLine(originX, originY + 5, originX, originY - numberOfYLabels * heightOfYValue - 5);
        g.fillPolygon(new int[]{originX - 5, originX + 5, originX},
                      new int[]{originY - numberOfYLabels * heightOfYValue - 5, originY - numberOfYLabels * heightOfYValue - 5, originY - numberOfYLabels * heightOfYValue - 10},3);
    }

    /**
     * Draws the x labels.
     *
     * @param g the graphics object
     * @param originX the x coordinate of origin
     * @param originY the y coordinate of origin
     * @param widthOfXValue the width of the x values
     */
    private void drawXLabels(Graphics g, int originX, int originY, int widthOfXValue) {
        int index = 0;
        int xLabelHeight = g.getFontMetrics().getHeight();

        for (XYValue value : barChart.getValues()) {
            g.drawLine(originX + index * widthOfXValue, originY, originX + index * widthOfXValue, originY + 5);
            g.drawString("" + value.getX(), (int) (originX + (index + 0.5) * widthOfXValue), originY + xLabelHeight);

            ++index;
        }

        g.drawLine(originX + index * widthOfXValue, originY, originX + index * widthOfXValue, originY + 5);

        g.drawLine(originX, originY, originX + index * widthOfXValue + 5, originY);
        g.fillPolygon(new int[]{originX + index * widthOfXValue + 5, originX + index * widthOfXValue + 5, originX + index * widthOfXValue + 10},
                      new int[]{originY - 5, originY + 5, originY}, 3);
    }

}
