package coloring.algorithms;

import java.util.Objects;

/**
 * Models a simple pixel using its x and y coordinates.
 *
 * @author Marko Lazarić
 */
public class Pixel {

    /**
     * The x coordinate of the pixel.
     */
    private final int x;

    /**
     * The y coordinate of the pixel.
     */
    private final int y;

    /**
     * Creates a new {@link Pixel} with the specified arguments.
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     */
    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of the pixel.
     *
     * @return the x coordinate of the pixel
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the pixel.
     *
     * @return the y coordinate of the pixel
     */
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pixel pixel = (Pixel) o;

        return x == pixel.x && y == pixel.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
