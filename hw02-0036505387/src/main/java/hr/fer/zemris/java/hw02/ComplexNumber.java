package hr.fer.zemris.java.hw02;

import java.util.Objects;

public class ComplexNumber {

	public static final ComplexNumber ONE = fromReal(1);
	public static final ComplexNumber MINUS_ONE = fromReal(-1);
	public static final ComplexNumber ZERO = fromReal(0);
	public static final ComplexNumber I = fromImaginary(1);
	public static final ComplexNumber MINUS_I = fromImaginary(-1);

	public static final double THRESHOLD = 1E-6;

	private final double real, imaginary;

	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}

	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}

	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		double real = magnitude * Math.cos(angle);
		double imaginary = magnitude * Math.sin(angle);

		return new ComplexNumber(real, imaginary);
	}

	public static ComplexNumber parse(String s) {
		String[] parts = s.split("(?=[+-])");

		ComplexNumber returnValue = ZERO;

		for (String part : parts) {
			returnValue = returnValue.add(parseSinglePart(part));
		}

		return returnValue;
	}

	private static ComplexNumber parseSinglePart(String s) {
		if (s.equals("i") || s.equals("+i")) {
			return ComplexNumber.I;
		} else if (s.equals("-i")) {
			return ComplexNumber.MINUS_I;
		}

		try {
			if (s.endsWith("i")) {
				double imaginary = Double.parseDouble(s.substring(0, s.length() - 1));

				return fromImaginary(imaginary);
			}

			double real = Double.parseDouble(s);

			return fromReal(real);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid complex number.");
		}
	}

	public double getReal() {
		return real;
	}

	public double getImaginary() {
		return imaginary;
	}

	public double getMagnitude() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}

	public double getAngle() {
		double angle = Math.atan2(imaginary, real);

		if (angle < 0) {
			return 2 * Math.PI + angle;
		}

		return angle;
	}

	public ComplexNumber conjugate() {
		return new ComplexNumber(real, -imaginary);
	}

	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(real + c.real, imaginary + c.imaginary);
	}

	public ComplexNumber sub(ComplexNumber c) {
		return add(c.mul(MINUS_ONE));
	}

	private ComplexNumber mul(double d) {
		return new ComplexNumber(real * d, imaginary * d);
	}

	public ComplexNumber mul(ComplexNumber c) {
		double real = this.real * c.real - this.imaginary * c.imaginary;
		double imaginary = this.real * c.imaginary + this.imaginary * c.real;

		return new ComplexNumber(real, imaginary);
	}

	private ComplexNumber div(double d) {
		return mul(1.0 / d);
	}

	public ComplexNumber div(ComplexNumber c) {
		if (c.equals(ZERO)) {
			throw new IllegalArgumentException("Error: cannot divide by zero.");
		}

		return mul(c.conjugate()).div(c.getMagnitude() * c.getMagnitude());
	}

	public ComplexNumber power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Cannot calculate power with a negative exponent.");
		} else if (n == 0) {
			return ONE;
		}

		double magnitude = Math.pow(getMagnitude(), n);
		double angle = n * getAngle();

		return fromMagnitudeAndAngle(magnitude, angle);
	}

	public ComplexNumber[] root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Cannot calculate negative roots.");
		}

		double magnitude = Math.pow(getMagnitude(), 1.0 / n);
		double angle = getAngle() / n;
		double angleStep = 2 * Math.PI / n;

		ComplexNumber[] roots = new ComplexNumber[n];

		for (int index = 0; index < n; ++index) {
			roots[index] = fromMagnitudeAndAngle(magnitude, angle + index * angleStep);
		}

		return roots;
	}

	@Override
	public int hashCode() {
		return Objects.hash(imaginary, real);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ComplexNumber)) {
			return false;
		}

		ComplexNumber other = (ComplexNumber) obj;

		return Math.abs(other.real - real) < THRESHOLD && Math.abs(other.imaginary - imaginary) < THRESHOLD;
	}

	@Override
	public String toString() {
		return String.format("%f%+fi", real, imaginary);
	}

}
