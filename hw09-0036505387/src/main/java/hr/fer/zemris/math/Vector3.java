package hr.fer.zemris.math;

public class Vector3 {

    private final double x;
    private final double y;
    private final double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 normalized() {
        double norm = norm();

        return new Vector3(x / norm, y / norm, z / norm);
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public Vector3 sub(Vector3 other) {
        return add(other.scale(-1));
    }

    public double dot(Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public Vector3 cross(Vector3 other) {
        //https://en.wikipedia.org/wiki/Cross_product#Computing_the_cross_product
        double x = this.y * other.z - this.z * other.y;
        double y = this.z * other.x - this.x * other.z;
        double z = this.x * other.y - this.y * other.x;

        return new Vector3(x, y, z);
    }

    public Vector3 scale(double s) {
        return new Vector3(s * x, s * y, s * z);
    }

    public double cosAngle(Vector3 other) {
        return this.dot(other) / (this.norm() * other.norm());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double[] toArray() {
        return new double[] {x, y, z};
    }

    @Override
    public String toString() {
        return String.format("(%.6f, %.6f, %.6f)", x, y, z);
    }

}
