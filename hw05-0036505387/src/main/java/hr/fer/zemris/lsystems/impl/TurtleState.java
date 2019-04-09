package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.math.Vector2D;

/**
 * Storage object for the state of the turtle.
 *
 * @author Marko Lazarić
 *
 */
public class TurtleState {

	/**
	 * The position of the turtle.
	 */
	private Vector2D position;

	/**
	 * The heading of the turtle.
	 * It should be normalised (its length should be exactly 1).
	 */
	private Vector2D heading;

	/**
	 * The color of the turtle's pen.
	 */
	private Color color;

	/**
	 * The scaler for the movement of the turtle.
	 */
	private double scaler;

	/**
	 * Creates a new {@link TurtleState} with the given arguments.
	 *
	 * @param position the position of the turtle
	 * @param heading the heading of the turtle
	 * @param color the color of its pen
	 * @param scaler the scaler for its movement
	 *
	 * @throws NullPointerException if {@code position}, {@code heading} or {@code color} is {@code null}
	 */
	public TurtleState(Vector2D position, Vector2D heading, Color color, double scaler) {
		this.position = Objects.requireNonNull(position, "Position of the turtle cannot be null.");
		this.heading = Objects.requireNonNull(heading, "Heading of the turtle cannot be null.");
		this.color = Objects.requireNonNull(color, "Color of the turtle's pen cannot be null.");
		this.scaler = scaler;
	}

	/**
	 * Creates a copy of the {@code this}.
	 *
	 * Important to notice: {@link Vector2D} is mutable so we have to create a new copy to prevent unwanted
	 * mutation. {@link Color} is immutable so we leave it as is and {@code scaler} is a primitive.
	 *
	 * @return the newly created copy.
	 */
	public TurtleState copy() {
		return new TurtleState(position.copy(), heading.copy(), color, scaler);
	}

	/**
	 * Returns the {@link #position} of the turtle.
	 *
	 * @return the {@link #position} of the turtle
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Sets the {@link #position} of the turtle.
	 *
	 * @param position the new {@link #position} of the turtle
	 *
	 * @throws NullPointerException if {@code position} is {@code null}
	 */
	public void setPosition(Vector2D position) {
		this.position = Objects.requireNonNull(position, "Position of the turtle cannot be null.");
	}

	/**
	 * Returns the {@link #heading} of the turtle.
	 *
	 * @return the {@link #heading} of the turtle
	 */
	public Vector2D getHeading() {
		return heading;
	}

	/**
	 * Sets the {@link #heading} of the turtle.
	 *
	 * @param heading the new {@link #heading} of the turtle
	 *
	 * @throws NullPointerException if {@code heading} is {@code null}
	 */
	public void setHeading(Vector2D heading) {
		this.heading = Objects.requireNonNull(heading, "Heading of the turtle cannot be null.");
	}

	/**
	 * Returns the {@link #color} of the turtle's pen.
	 *
	 * @return the {@link #color} of the turtle's pen
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the {@link #color} of the turtle's pen.
	 *
	 * @param color the new {@link #color} of the turtle's pen
	 *
	 * @throws NullPointerException if {@code color} is {@code null}
	 */
	public void setColor(Color color) {
		this.color = Objects.requireNonNull(color, "Color of the turtle's pen cannot be null.");
	}

	/**
	 * Returns the {@link #scaler} for the movement of the turtle.
	 *
	 * @return the {@link #scaler} for the movement of the turtle
	 */
	public double getScaler() {
		return scaler;
	}

	/**
	 * Sets the {@link #scaler} for the movement of the turtle.
	 *
	 * @param scaler the new {@link #scaler} for the movement of the turtle
	 */
	public void setScaler(double scaler) {
		this.scaler = scaler;
	}

}
