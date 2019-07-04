package hr.fer.zemris.java.hw17.jvdraw.geometrical.visitor;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.object.Line;

public interface GeometricalObjectVisitor {

    void visit(Line line);
    void visit(Circle circle);
    void visit(FilledCircle filledCircle);

}
