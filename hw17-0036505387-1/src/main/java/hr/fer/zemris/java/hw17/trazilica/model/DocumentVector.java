package hr.fer.zemris.java.hw17.trazilica.model;

import java.nio.file.Path;
import java.util.Objects;

import static java.lang.Math.sqrt;

/**
 * Models a document represented by its TF-IDF vector.
 *
 * @author Marko Lazarić
 */
public class DocumentVector {

    /**
     * The path to the file whose {@link DocumentVector} this is.
     */
    private final Path pathToDocument;

    /**
     * The generated TF-IDF vector.
     */
    private final double[] vector;

    /**
     * Creates a new {@link DocumentVector} with the given argument.
     *
     * @param vector the generated TF-IDF vector
     */
    public DocumentVector(double[] vector) {
        this(vector, null);
    }

    /**
     * Creates a new {@link DocumentVector} with the given arguments.
     *
     * @param vector the generated TF-IDF vector
     * @param pathToDocument the path to the file whose {@link DocumentVector} this is
     */
    public DocumentVector(double[] vector, Path pathToDocument) {
        this.pathToDocument = pathToDocument;
        this.vector = Objects.requireNonNull(vector, "Vector cannot be null.");
    }

    /**
     * Returns the norm of the vector (its length).
     *
     * @return the norm of the vector
     */
    public double norm() {
        return sqrt(this.scalarProduct(this));
    }

    /**
     * Calculates the scalar product between this vector and the argument.
     *
     * @param other the second argument to the scalar vector
     * @return the calculated scalar vector
     */
    public double scalarProduct(DocumentVector other) {
        double result = 0;

        for (int index = 0; index < vector.length; ++index) {
            result += this.vector[index] * other.vector[index];
        }

        return result;
    }

    /**
     * Calculates the similarity between two {@link DocumentVector}s.
     *
     * @param other the second {@link DocumentVector} to compare this to
     * @return a number from 0 to 1 representing how similar the documents are
     */
    public double similarity(DocumentVector other) {
        return this.scalarProduct(other) / (this.norm() * other.norm());
    }

    /**
     * Returns the path to the file whose document this is.
     *
     * @return the path to the file whose document this is
     */
    public Path getPathToDocument() {
        return pathToDocument;
    }

    /**
     * Returns the generated TF-IDF vector.
     *
     * @return the generated TF-IDF vector
     */
    public double[] getVector() {
        return vector;
    }

}
