package hr.fer.zemris.java.gui.charts;

/**
 * Models a pair of values representing a bar in a bar chart.
 *
 * @author Marko Lazarić
 */
public class XYValue {

    /**
     * The x component.
     */
    private final int x;

    /**
     * The y component.
     */
    private final int y;

    /**
     * Creates a new {@link XYValue} with the given arguments.
     *
     * @param x the x component
     * @param y the y component
     */
    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x component.
     *
     * @return the x component.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y component.
     *
     * @return the y component.
     */
    public int getY() {
        return y;
    }
}
