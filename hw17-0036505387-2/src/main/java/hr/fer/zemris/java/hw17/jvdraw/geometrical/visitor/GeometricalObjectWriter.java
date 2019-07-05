package hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

/**
 * A {@link GeometricalObjectVisitor} which writes all the {@link GeometricalObject} it visits to a {@link Writer}.
 *
 * @author Marko Lazarić
 */
public class GeometricalObjectWriter implements GeometricalObjectVisitor {

    /**
     * The {@link Writer} used for writing the {@link GeometricalObject}s.
     */
    private final Writer writer;

    /**
     * Creates a new {@link GeometricalObjectWriter} with the given argument.
     *
     * @param writer the {@link Writer} used for writing the {@link GeometricalObject}s
     *
     * @throws NullPointerException if the argument is null
     */
    public GeometricalObjectWriter(Writer writer) {
        this.writer = Objects.requireNonNull(writer, "Writer cannot be null.");
    }

    @Override
    public void visit(Line line) {
        try {
            writer.write(
                    String.format(
                            "LINE %d %d %d %d %d %d %d%n",
                            line.getStart().x,
                            line.getStart().y,
                            line.getEnd().x,
                            line.getEnd().y,
                            line.getColor().getRed(),
                            line.getColor().getGreen(),
                            line.getColor().getBlue()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(Circle circle) {
        try {
            writer.write(
                    String.format(
                            "CIRCLE %d %d %d %d %d %d%n",
                            circle.getCenter().x,
                            circle.getCenter().y,
                            circle.getRadius(),
                            circle.getOutlineColor().getRed(),
                            circle.getOutlineColor().getGreen(),
                            circle.getOutlineColor().getBlue()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(FilledCircle filledCircle) {
        try {
            writer.write(
                    String.format(
                            "FCIRCLE %d %d %d %d %d %d %d %d %d%n",
                            filledCircle.getCenter().x,
                            filledCircle.getCenter().y,
                            filledCircle.getRadius(),
                            filledCircle.getOutlineColor().getRed(),
                            filledCircle.getOutlineColor().getGreen(),
                            filledCircle.getOutlineColor().getBlue(),
                            filledCircle.getFillColor().getRed(),
                            filledCircle.getFillColor().getGreen(),
                            filledCircle.getFillColor().getBlue()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
