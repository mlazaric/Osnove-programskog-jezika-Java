package hr.fer.zemris.java.hw17.jvdraw.geometrical;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.objects.Line;

public interface GeometricalObjectVisitor {

    void visit(Line line);
    void visit(Circle circle);
    void visit(FilledCircle filledCircle);

}
