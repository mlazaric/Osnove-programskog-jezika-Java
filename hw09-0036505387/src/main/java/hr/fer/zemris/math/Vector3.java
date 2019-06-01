package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Models an immutable 3D vector represented by its three components.
 *
 * @author Marko Lazarić
 */
public class Vector3 {

    /**
     * The x component of the vector.
     */
    private final double x;

    /**
     * The y component of the vector.
     */
    private final double y;

    /**
     * The z component of the vector.
     */
    private final double z;

    /**
     * Creates a new {@link Vector3} with the given arguments.
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @param z the z component of the vector
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the norm of the vector (its length).
     *
     * @return the norm of the vector
     */
    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Returns a new vector with the same direction as {@code this} but with length 1.
     *
     * @return the normalized vector
     */
    public Vector3 normalized() {
        double norm = norm();

        return new Vector3(x / norm, y / norm, z / norm);
    }

    /**
     * Returns a new vector which represents the sum of {@code this} and {@code other}.
     *
     * @param other the vector to sum with {@code this}
     * @return a new vector which represents the sum of {@code this} and {@code other}
     * 
     * @throws NullPointerException if {@code other} is {@code null}
     */
    public Vector3 add(Vector3 other) {
        Objects.requireNonNull(other, "Cannot add null to vector3.");
        
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Returns a new vector which represents the result of subtracting {@code other} from {@code this}.
     *
     * @param other the vector to sub from {@code this}
     * @return a new vector which represents the result of subtracting {@code other} from {@code this}
     * 
     * @throws NullPointerException if {@code other} is {@code null}
     */
    public Vector3 sub(Vector3 other) {
        Objects.requireNonNull(other, "Cannot sub null from vector3.");
        
        return add(other.scale(-1));
    }

    /**
     * Computes the dot product between {@code this} and {@code other}.
     * 
     * @param other the second argument of the dot product
     * @return the dot product between {@code this} and {@code other}
     *
     * @throws NullPointerException if {@code other} is {@code null}
     */
    public double dot(Vector3 other) {
        Objects.requireNonNull(other, "Cannot calculate dot product with null.");

        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Computes the cross product between {@code this} and {@code other}.
     *
     * @param other the second argument of the cross product
     * @return the cross product between {@code this} and {@code other}
     *
     * @throws NullPointerException if {@code other} is {@code null}
     */
    public Vector3 cross(Vector3 other) {
        Objects.requireNonNull(other, "Cannot calculate cross product with null.");

        //https://en.wikipedia.org/wiki/Cross_product#Computing_the_cross_product
        double x = this.y * other.z - this.z * other.y;
        double y = this.z * other.x - this.x * other.z;
        double z = this.x * other.y - this.y * other.x;

        return new Vector3(x, y, z);
    }

    /**
     * Creates a new vector which represents {@code this} scaled by {@code s}.
     *
     * @param s the scaling factor
     * @return a new vector which represents {@code this} scaled by {@code s}
     */
    public Vector3 scale(double s) {
        return new Vector3(s * x, s * y, s * z);
    }

    /**
     * Calculates the cos of angle between {@code this} and {@code other}.
     *
     * @param other the other vector
     * @return the cos of angle between {@code this} and {@code other}
     *
     * @throws NullPointerException if {@code other} is {@code null}
     */
    public double cosAngle(Vector3 other) {
        Objects.requireNonNull(other, "Cannot calculate cos angle with null.");

        return this.dot(other) / (this.norm() * other.norm());
    }

    /**
     * Returns the x component of the vector.
     *
     * @return the x component
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y component of the vector.
     *
     * @return the y component
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the z component of the vector.
     *
     * @return the z component
     */
    public double getZ() {
        return z;
    }

    /**
     * Returns an array of three doubles where each represents one of the components of the vector.
     *
     * @return an array of three doubles representing the vector
     */
    public double[] toArray() {
        return new double[] {x, y, z};
    }

    @Override
    public String toString() {
        return String.format("(%.6f, %.6f, %.6f)", x, y, z);
    }

}
