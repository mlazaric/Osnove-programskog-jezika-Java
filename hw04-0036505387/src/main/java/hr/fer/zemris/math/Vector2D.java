package hr.fer.zemris.math;

public class Vector2D {

	private double x, y;

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void translate(Vector2D offset) {
		x += offset.x;
		y += offset.y;
	}

	public Vector2D translated(Vector2D offset) {
		Vector2D vector = copy();

		vector.translate(offset);

		return vector;
	}

	public void rotate(double angle) {
		double currentAngle = Math.atan2(y, x);
		double magnitude = Math.sqrt(x * x + y * y);

		x = magnitude * Math.cos(angle + currentAngle);
		y = magnitude * Math.sin(angle + currentAngle);
	}

	public Vector2D rotated(double angle) {
		Vector2D vector = copy();

		vector.rotate(angle);

		return vector;
	}

	public void scale(double scaler) {
		x *= scaler;
		y *= scaler;
	}

	public Vector2D scaled(double scaler) {
		Vector2D vector = copy();

		vector.scale(scaler);

		return vector;
	}

	public Vector2D copy() {
		return new Vector2D(x, y);
	}
}
