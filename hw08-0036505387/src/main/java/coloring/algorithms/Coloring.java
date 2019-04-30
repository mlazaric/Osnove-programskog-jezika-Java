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

public class Coloring implements Consumer<Pixel>,
                                 Function<Pixel, List<Pixel>>,
                                 Predicate<Pixel>,
                                 Supplier<Pixel> {

    private final Pixel reference;
    private final Picture picture;
    private final int fillColor;
    private final int refColor;

    public Coloring(Pixel reference, Picture picture, int fillColor) {
        this.reference = Objects.requireNonNull(reference, "Cannot use null as a reference pixel.");
        this.picture = Objects.requireNonNull(picture, "Cannot use null as a picture.");
        this.fillColor = fillColor;

        this.refColor = picture.getPixelColor(reference.getX(), reference.getY());
    }

    private boolean isValidPixel(Pixel pixel) {
        int x = pixel.getX();
        int y = pixel.getY();
        int width = picture.getWidth();
        int height = picture.getHeight();

        return x >= 0 && x < width && y >= 0 && y < height;
    }

    @Override
    public void accept(Pixel pixel) {
        if (!isValidPixel(pixel)) return;

        picture.setPixelColor(pixel.getX(), pixel.getY(), fillColor);
    }

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

    @Override
    public boolean test(Pixel pixel) {
        if (!isValidPixel(pixel)) return false;

        return picture.getPixelColor(pixel.getX(), pixel.getY()) == refColor;
    }

    @Override
    public Pixel get() {
        return reference;
    }
}
