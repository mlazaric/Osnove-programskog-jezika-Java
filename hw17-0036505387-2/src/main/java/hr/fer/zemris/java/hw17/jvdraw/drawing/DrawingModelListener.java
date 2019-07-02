package hr.fer.zemris.java.hw17.jvdraw.drawing;

public interface DrawingModelListener {

    void objectsAdded(DrawingModel source, int firstIndex, int lastIndex);

    void objectsRemoved(DrawingModel source, int firstIndex, int lastIndex);

    void objectsChanged(DrawingModel source, int firstIndex, int lastIndex);

}
