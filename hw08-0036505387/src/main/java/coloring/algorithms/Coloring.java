package coloring.algorithms;

import marcupic.opjj.statespace.coloring.Picture;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Models a simple action of replacing the {@code refColor} with {@code fillColor} connected to {@code reference}.
 *
 * @author Marko Lazarić
 */
public class Coloring implements Consumer<Pixel>,
                                 Function<Pixel, List<Pixel>>,
                                 Predicate<Pixel>,
                                 Supplier<Pixel> {

    /**
     * The referenced pixel.
     */
    private final Pixel reference;

    /**
     * The referenced picture.
     */
    private final Picture picture;

    /**
     * The color to replace with.
     */
    private final int fillColor;

    /**
     * The color to be replaced.
     */
    private final int refColor;

    /**
     * Creates a new {@link Coloring} object with the specified arguments.
     *
     * @param reference the referenced pixel
     * @param picture the referenced picture
     * @param fillColor the color to replace with
     *
     * @throws NullPointerException if either {@code reference} or {@code picture} is {@code null}
     */
    public Coloring(Pixel reference, Picture picture, int fillColor) {
        this.reference = Objects.requireNonNull(reference, "Cannot use null as a reference pixel.");
        this.picture = Objects.requireNonNull(picture, "Cannot use null as a picture.");
        this.fillColor = fillColor;

        this.refColor = picture.getPixelColor(reference.getX(), reference.getY());
    }

    /**
     * Returns whether the pixel is within the referenced picture.
     *
     * @param pixel the pixel to check
     * @return true if it is within the referenced picture, false otherwise
     */
    private boolean isValidPixel(Pixel pixel) {
        int x = pixel.getX();
        int y = pixel.getY();
        int width = picture.getWidth();
        int height = picture.getHeight();

        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Sets pixel color to {@link #fillColor}.
     *
     * @param pixel the pixel to set
     */
    @Override
    public void accept(Pixel pixel) {
        if (!isValidPixel(pixel)) return;

        picture.setPixelColor(pixel.getX(), pixel.getY(), fillColor);
    }

    /**
     * Returns neighbours of the pixel.
     *
     * @param pixel the pixel whose neighbours it should return
     * @return the neighbours of the pixel
     */
    @Override
    public List<Pixel> apply(Pixel pixel) {
        if (!isValidPixel(pixel)) return Collections.emptyList();

        int x = pixel.getX();
        int y = pixel.getY();

        return Stream.of(new Pixel(x - 1, y),
                         new Pixel(x + 1, y),
                         new Pixel(x, y - 1),
                         new Pixel(x, y + 1))
                .filter(this::isValidPixel)
                .collect(Collectors.toList());
    }

    /**
     * Tests whether the pixel's color is {@link #refColor}.
     *
     * @param pixel the pixel whose color it should test
     * @return true if it is {@link #refColor}, false otherwise
     */
    @Override
    public boolean test(Pixel pixel) {
        if (!isValidPixel(pixel)) return false;

        return picture.getPixelColor(pixel.getX(), pixel.getY()) == refColor;
    }

    /**
     * Returns the referenced pixel.
     *
     * @return the referenced pixel
     */
    @Override
    public Pixel get() {
        return reference;
    }
}
