package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Models a complex number represented by two doubles: {@link #real} and {@link #imaginary}.
 * Implements methods for manipulating and calculating using complex numbers.
 *
 * @author Marko Lazarić
 *
 */
public class Complex {

    // Constants

    /**
     * Constant representing "1".
     */
    public static final Complex ONE = fromReal(1);

    /**
     * Constant representing "-1".
     */
    public static final Complex ONE_NEG = fromReal(-1);

    /**
     * Constant representing "0".
     */
    public static final Complex ZERO = fromReal(0);

    /**
     * Constant representing "i".
     */
    public static final Complex IM = fromImaginary(1);

    /**
     * Constant representing "-i".
     */
    public static final Complex IM_NEG = fromImaginary(-1);


    /**
     * Precision threshold for determining equality.
     */
    public static final double THRESHOLD = 1E-6;


    // Attributes

    /**
     * Real part of the complex number.
     */
    private final double real;

    /**
     * Imaginary part of the complex number.
     */
    private final double imaginary;

    // Constructors and static helper functions for creating complex numbers

    /**
     * Creates a new complex number with the given real and imaginary parts.
     *
     * @param real real part of the complex number
     * @param imaginary imaginary part of the complex number
     */
    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Creates a complex number with the given real part and uses 0 for the imaginary part.
     *
     * @param real real part of the complex number
     * @return the complex number
     */
    public static Complex fromReal(double real) {
        return new Complex(real, 0);
    }

    /**
     * Creates a complex number with the given imaginary part and uses 0 for the real part.
     *
     * @param imaginary imaginary part of the complex number
     * @return the complex number
     */
    public static Complex fromImaginary(double imaginary) {
        return new Complex(0, imaginary);
    }

    /**
     * Creates a complex number from the polar representation (magnitude and angle).
     *
     * @param magnitude magnitude of the complex number
     * @param angle angle of the complex number
     * @return the complex number
     */
    public static Complex fromMagnitudeAndAngle(double magnitude, double angle) {
        double real = magnitude * Math.cos(angle);
        double imaginary = magnitude * Math.sin(angle);

        return new Complex(real, imaginary);
    }

    /**
     * Parses a complex number from a string. The following strings are considered valid complex numbers:
     *
     * <ul>
     * 	  <li>Pure real numbers: "351", "-317", "3.51", "-3.17"</li>
     * 	  <li>Pure imaginary numbers: "351i", "-317i", "3.51i", "-3.17i"</li>
     *    <li>IM without anything else: "i", "-i"</li>
     *    <li>Complex numbers with both real and imaginary parts: "-1-i", "31+24i"</li>
     *    <li>Complex numbers with a leading plus sign: "+1-i", "+31+24i"</li>
     * </ul>
     *
     * The following strings are not considered valid complex numbers:
     *
     * <ul>
     * 	  <li>Those with IM before the magnitude of the imaginary part: "i351", "-i317", "i3.51", "-i3.17"</li>
     * 	  <li>Strings containing multiple signs: "+-351i", "1--317i", "0++3.51i", "--1-3.17i"</li>
     * </ul>
     *
     * @param s the string to parse
     * @return the parsed complex number
     *
     * @throws IllegalArgumentException if the string is not a valid complex number
     * @throws NullPointerException if {@code s} is {@code null}
     */
    public static Complex parse(String s) {
        Objects.requireNonNull(s, "Cannot parse null.");

        if (s.isBlank()) {
            throw new IllegalArgumentException("Invalid complex number: blank string.");
        }

        String[] parts = s.split("(?=[+-])");

        if (parts.length > 2) {
            throw new IllegalArgumentException("Invalid complex number: too many terms.");  // Don't parse "2i+3i+5+8"
        }

        Complex returnValue = ZERO;
        boolean hasRealPart, hasImaginaryPart;

        hasRealPart = hasImaginaryPart = false;

        for (String part : parts) {
            Complex term = parseSingleTerm(part);

            returnValue = returnValue.add(term);

            if (Math.abs(term.real) > 0) { // Don't parse "2i+3i" or "2+3"
                if (hasRealPart) {
                    throw new IllegalArgumentException("Invalid complex number: it has two real terms");
                }

                hasRealPart = true;
            }
            else if (Math.abs(term.imaginary) > 0) {
                if (hasImaginaryPart) {
                    throw new IllegalArgumentException("Invalid complex number: it has two imaginary terms");
                }

                hasImaginaryPart = true;
            }
        }

        return returnValue;
    }

    /**
     * Parses a string containing a single term of the expression.
     * Every real and imaginary number in the string is one term.</br>
     * <br/>
     * For example, the terms of "2.51+i" are "2.51" and "i", so it should be
     * called as {@code parseSingleTerm("2.51").add(parseSingleTerm("i"))} to
     * parse the full expression.
     *
     * @param s a single term from the expression
     * @return the parsed term
     *
     * @throws IllegalArgumentException if the term is not a valid complex number
     */
    private static Complex parseSingleTerm(String s) {
        if (s.equals("i") || s.equals("+i")) {
            return Complex.IM;
        } else if (s.equals("-i")) {
            return Complex.IM_NEG;
        }

        try {
            if (s.endsWith("i")) {
                double imaginary = Double.parseDouble(s.substring(0, s.length() - 1));

                return fromImaginary(imaginary);
            }

            double real = Double.parseDouble(s);

            return fromReal(real);
        } catch (NumberFormatException e) {
            String message = String.format("Invalid complex number: '%s' is not a valid term", s);

            throw new IllegalArgumentException(message);
        }
    }

    // Getters

    /**
     * Returns the real part of the complex number.
     *
     * @return the real part of the complex number
     *
     * @see #real
     */
    public double getReal() {
        return real;
    }

    /**
     * Returns the imaginary part of the complex number.
     *
     * @return the imaginary part of the complex number
     *
     * @see #imaginary
     */
    public double getImaginary() {
        return imaginary;
    }

    /**
     * Returns the magnitude (modulus) of the complex number.
     *
     * @return the magnitude (modulus) of the complex number
     *
     * @see <a href="https://en.wikipedia.org/wiki/Complex_number#Polar_complex_plane">https://en.wikipedia.org/wiki/Complex_number#Polar_complex_plane</a>
     */
    public double module() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    /**
     * Returns the angle (argument) of the complex number. The angle can be in the [0, 2PI] range.
     *
     * @return the angle (argument) of the complex number
     *
     * @see <a href="https://en.wikipedia.org/wiki/Complex_number#Polar_complex_plane">https://en.wikipedia.org/wiki/Complex_number#Polar_complex_plane</a>
     */
    public double getAngle() {
        double angle = Math.atan2(imaginary, real);

        if (angle < 0) {
            return 2 * Math.PI + angle;
        }

        return angle;
    }

    // Methods for calculating and manipulating complex numbers

    /**
     * Returns a conjugated a complex number.
     *
     * @return a new complex number representing the conjugated complex number
     *
     * @see <a href="https://en.wikipedia.org/wiki/Complex_number#Conjugate">https://en.wikipedia.org/wiki/Complex_number#Conjugate</a>
     */
    public Complex conjugate() {
        return new Complex(real, -imaginary);
    }

    /**
     * Sums two complex numbers and returns the result.
     *
     * @param c the complex number to add to {@code this}
     * @return a new complex number representing the sum of {@code this} and {@code c}
     *
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Complex add(Complex c) {
        Objects.requireNonNull(c, "Cannot add null to a complex number.");

        return new Complex(real + c.real, imaginary + c.imaginary);
    }

    /**
     * Subtracts two complex numbers and returns the result.
     *
     * @param c the complex number to subtract from {@code this}
     * @return a new complex number representing the result of subtracting {@code c} from {code this}
     *
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Complex subtract(Complex c) {
        Objects.requireNonNull(c, "Cannot subtract null from a complex number.");

        return add(c.multiply(ONE_NEG));
    }

    public Complex negate() {
        return this.multiply(ONE_NEG);
    }

    /**
     * Multiplies a complex number by a scalar and returns the result.
     *
     * @param d the scalar with which to multiply the complex number
     * @return a new complex number representing the result of multiplying {@code this} by {@code d}
     */
    public Complex multiply(double d) {
        return new Complex(real * d, imaginary * d);
    }

    /**
     * Multiplies two complex numbers and returns the result.
     *
     * @param c the complex number to multiply with {@code this}
     * @return a new complex number representing the result of multiplying {@code this} with {@code d}
     *
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Complex multiply(Complex c) {
        Objects.requireNonNull(c, "Cannot multiply a complex number with null.");

        double real = this.real * c.real - this.imaginary * c.imaginary;
        double imaginary = this.real * c.imaginary + this.imaginary * c.real;

        return new Complex(real, imaginary);
    }

    /**
     * Divides a complex number by a scalar and returns the result.
     *
     * @param d the scalar with which to divide the complex number
     * @return a new complex number representing the result of dividing {@code this} by {@code d}
     */
    public Complex divide(double d) {
        return multiply(1.0 / d);
    }

    /**
     * Divides two complex numbers and returns the result.
     *
     * @param c the complex number to divide {@code this} by
     * @return a new complex number representing the result of dividing {@code this} by {@code c}
     *
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Complex divide(Complex c) {
        Objects.requireNonNull(c, "Cannot divide a complex number with null.");

        return multiply(c.conjugate()).divide(c.module() * c.module());
    }

    /**
     * Raises {@code this} to the {@code n}-th power and returns the result. N should be greater than
     * or equal to 0.
     *
     * @param n the power to raise {@code this} to
     * @return a new complex number representing the result of raising {@code this} to the {@code n}-th power
     *
     * @throws IllegalArgumentException if n is less than 0
     */
    public Complex power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot calculate power with a negative exponent.");
        } else if (n == 0) {
            return ONE;
        }

        double magnitude = Math.pow(module(), n);
        double angle = n * getAngle();

        return fromMagnitudeAndAngle(magnitude, angle);
    }

    /**
     * Calculates the {@code n}-th roots of {@code this} and returns the result. N should be greater than 0.
     *
     * @param n the degree of the roots to calculate
     * @return an array with n elements representing the {@code n}-th roots of {@code this}
     *
     * @throws IllegalArgumentException if n is lesser than or equal to 0
     *
     * @see <a href="https://en.wikipedia.org/wiki/Complex_number#Integer_and_fractional_exponents">https://en.wikipedia.org/wiki/Complex_number#Integer_and_fractional_exponents</a>
     */
    public List<Complex> root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Cannot calculate negative roots.");
        }

        double magnitude = Math.pow(module(), 1.0 / n);
        double angle = getAngle() / n;
        double angleStep = 2 * Math.PI / n;

        List<Complex> roots = new ArrayList<>(n);

        for (int index = 0; index < n; ++index) {
            roots.add(fromMagnitudeAndAngle(magnitude, angle + index * angleStep));
        }

        return roots;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(imaginary, real);
    }

    /**
     * Checks equality between {@code this} and {@code obj}.
     *
     * Two complex numbers are considered equal if and only if the
     * absolute difference of their real and imaginary parts is lesser
     * than {@link #THRESHOLD}.
     *
     * @param obj the object being compared to {@code this}
     * @returns true if they are equal, false otherwise
     *
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Complex)) {
            return false;
        }

        Complex other = (Complex) obj;

        return Math.abs(other.real - real) < THRESHOLD && Math.abs(other.imaginary - imaginary) < THRESHOLD;
    }

    /**
     * Returns a string representing the complex number.
     *
     * @return the string representation of the complex number
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("(%f%+fi)", real, imaginary);
    }

}