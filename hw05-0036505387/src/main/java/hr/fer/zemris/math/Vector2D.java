package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Models a simple vector in 2 dimensions represented as its x and y components.
 *
 * @author Marko Lazarić
 *
 */
public class Vector2D {

	/**
	 * The precision used to determine if two vectors are equal.
	 *
	 * @see #equals(Object)
	 */
	private static double THRESHOLD = 1E-6;

	/**
	 * The x component of the vector.
	 */
	private double x;

	/**
	 * The y component of the vector.
	 */
	private double y;

	/**
	 * Constructs a new {@link Vector2D} with the given arguments.
	 *
	 * @param x the x component of the vector
	 * @param y the y component of the vector
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the x component of the vector.
	 *
	 * @return the x component of the vector
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y component of the vector.
	 *
	 * @return the y component of the vector
	 */
	public double getY() {
		return y;
	}

	/**
	 * Translates {@code this} vector by the given {@code offset} vector.
	 *
	 * @param offset the vector to translate this vector by
	 *
	 * @throws NullPointerException if {@code offset} is {@code null}
	 */
	public void translate(Vector2D offset) {
		Objects.requireNonNull(offset, "Offset cannot be null.");

		x += offset.x;
		y += offset.y;
	}

	/**
	 * Copies {@code this} vector and translates the newly created vector
	 * by the {@code offset} vector.
	 *
	 * @param offset the vector to translate this vector by
	 * @return a newly created vector which represents the sum of {@code this}
	 *         and {@code offset} vectors
	 *
	 * @throws NullPointerException if {@code offset} is {@code null}
	 */
	public Vector2D translated(Vector2D offset) {
		Vector2D vector = copy();

		vector.translate(offset);

		return vector;
	}

	/**
	 * Rotates {@code this} vector by {@code angle}.
	 *
	 * @param angle the angle to rotate this vector by
	 */
	public void rotate(double angle) {
		double currentAngle = Math.atan2(y, x);
		double magnitude = Math.sqrt(x * x + y * y);

		x = magnitude * Math.cos(angle + currentAngle);
		y = magnitude * Math.sin(angle + currentAngle);
	}

	/**
	 * Copies {@code this} vector and rotates the newly created vector
	 * by {@code angle}.
	 *
	 * @param angle the angle to rotate this vector by
	 * @return a newly created vector which represents {@code this} vector
	 *         rotated by {@code angle}
	 */
	public Vector2D rotated(double angle) {
		Vector2D vector = copy();

		vector.rotate(angle);

		return vector;
	}

	/**
	 * Scales {@code this} vector with the given {@code scaler}.
	 *
	 * @param scaler the number to scale {@code this} vector by
	 */
	public void scale(double scaler) {
		x *= scaler;
		y *= scaler;
	}

	/**
	 * Copies {@code this} vector and scales the newly created vector
	 * with the {@code scaler}.
	 *
	 * @param scaler the number to scale the vector by
	 * @return a newly created vector which represents {@code this} multiplied
	 *         by {@code scaler}
	 */
	public Vector2D scaled(double scaler) {
		Vector2D vector = copy();

		vector.scale(scaler);

		return vector;
	}

	/**
	 * Copies {@code this} vector.
	 *
	 * @return the newly created {@link Vector2D} which is equal to {@code this}
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Vector2D)) {
			return false;
		}

		Vector2D other = (Vector2D) obj;

		return Math.abs(x - other.x) < THRESHOLD && Math.abs(y - other.y) < THRESHOLD;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "Vector2D [x=" + x + ", y=" + y + "]";
	}

}
