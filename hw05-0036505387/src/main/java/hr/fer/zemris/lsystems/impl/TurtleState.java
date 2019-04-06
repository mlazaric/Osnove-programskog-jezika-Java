package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.math.Vector2D;

public class TurtleState {

	private Vector2D position;
	private Vector2D heading;
	private Color color;
	private double scaler;

	public TurtleState(Vector2D position, Vector2D heading, Color color, double scaler) {
		this.position = position;
		this.heading = heading;
		this.color = color;
		this.scaler = scaler;
	}


	public TurtleState copy() {
		return new TurtleState(position.copy(), heading.copy(), color, scaler);
	}


	public Vector2D getPosition() {
		return position;
	}


	public void setPosition(Vector2D position) {
		this.position = position;
	}


	public Vector2D getHeading() {
		return heading;
	}


	public void setHeading(Vector2D heading) {
		this.heading = heading;
	}


	public Color getColor() {
		return color;
	}


	public void setColor(Color color) {
		this.color = color;
	}


	public double getScaler() {
		return scaler;
	}


	public void setScaler(double scaler) {
		this.scaler = scaler;
	}
}
