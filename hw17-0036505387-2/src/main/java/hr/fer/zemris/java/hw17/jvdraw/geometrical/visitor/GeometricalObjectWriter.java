package hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;

import java.io.IOException;
import java.io.Writer;

public class GeometricalObjectWriter implements GeometricalObjectVisitor {

    private final Writer writer;

    public GeometricalObjectWriter(Writer writer) {
        this.writer = writer;
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
