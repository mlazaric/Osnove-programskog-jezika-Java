package hr.fer.zemris.java.hw17.trazilica.model;

import java.nio.file.Path;
import java.util.Objects;

import static java.lang.Math.sqrt;

public class DocumentVector {

    private final Path pathToDocument;
    private final double[] vector;

    public DocumentVector(double[] vector) {
        this(vector, null);
    }

    public DocumentVector(double[] vector, Path pathToDocument) {
        this.pathToDocument = pathToDocument;
        this.vector = Objects.requireNonNull(vector, "Vector cannot be null.");
    }

    public double norm() {
        return sqrt(this.scalarProduct(this));
    }

    public double scalarProduct(DocumentVector other) {
        double result = 0;

        for (int index = 0; index < vector.length; ++index) {
            result += this.vector[index] * other.vector[index];
        }

        return result;
    }

    public double similarity(DocumentVector other) {
        return this.scalarProduct(other) / (this.norm() * other.norm());
    }

    public Path getPathToDocument() {
        return pathToDocument;
    }

    public double[] getVector() {
        return vector;
    }
}
