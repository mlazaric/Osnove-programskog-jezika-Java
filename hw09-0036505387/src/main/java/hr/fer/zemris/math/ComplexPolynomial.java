package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * Models a complex polynomial represented by a list of coefficients.
 *
 * @author Marko Lazarić
 */
public class ComplexPolynomial {

    /**
     * The list of coefficients multiplying the various z^n.
     */
    private final Complex[] factors;

    /**
     * Creates a new {@link ComplexPolynomial} with the given factors.
     *
     * @param factors the coefficients multiplying the various z^n
     *
     * @throws NullPointerException if {@code factors} is {@code null}
     */
    public ComplexPolynomial(Complex ...factors) {
        this.factors = Objects.requireNonNull(factors, "Factors cannot be null.");
    }

    /**
     * Returns the order of the polynomial.
     *
     * @return the order of the polynomial
     */
    public short order() {
        return (short) (factors.length - 1);
    }

    /**
     * Multiplies two {@link ComplexPolynomial}s and returns the result.
     *
     * @param p the second polynomial
     * @return the result of the multiplication
     *
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        Objects.requireNonNull(p, "Cannot multiply polynomial with null");

        Complex[] newFactors = new Complex[p.order() + this.order() + 1];

        Arrays.fill(newFactors, Complex.ZERO);

        for (int n1 = 0; n1 < p.factors.length; ++n1) {
            for (int n2 = 0; n2 < this.factors.length; ++n2) {
                newFactors[n1 + n2] = newFactors[n1 + n2].add(p.factors[n1].multiply(factors[n2]));
            }
        }

        return new ComplexPolynomial(newFactors);
    }

    /**
     * Derives the {@link ComplexPolynomial} and returns the result.
     *
     * @return the derivation of {@code this} polynomial
     */
    public ComplexPolynomial derive() {
        Complex[] newFactors = new Complex[factors.length - 1];

        for (int n = 1; n < factors.length; ++n) {
            newFactors[n - 1] = factors[n].multiply(new Complex(n, 0));
        }

        return new ComplexPolynomial(newFactors);
    }

    /**
     * Calculates the value of this polynomial at the point specified by the argument.
     *
     * @param z the point to evaluated the polynomial at
     * @return the result of evaluating the polynomial at that point
     *
     * @throws NullPointerException if {@code z} is {@code null}
     */
    public Complex apply(Complex z) {
        Objects.requireNonNull(z, "Cannot apply polynomial to null.");

        Complex result = Complex.ZERO;
        Complex zn = Complex.ONE; // z^0

        for (int n = 0; n < factors.length; ++n) {
            result = result.add(zn.multiply(factors[n]));
            zn = zn.multiply(z);
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int n = factors.length - 1; n >= 0; --n) {
            if (n == 0) {
                sb.append('+').append(factors[n]);
            }
            else if (n == factors.length - 1) {
                sb.append(factors[n]).append("*z^").append(n);
            }
            else {
                sb.append('+').append(factors[n]).append("*z^").append(n);
            }
        }

        return sb.toString();
    }
}
