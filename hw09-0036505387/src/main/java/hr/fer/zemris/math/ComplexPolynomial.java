package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Objects;

public class ComplexPolynomial {

    private final Complex[] factors;

    public ComplexPolynomial(Complex ...factors) {
        this.factors = Objects.requireNonNull(factors, "Factors cannot be null.");
    }

    public short order() {
        return (short) (factors.length - 1);
    }

    public ComplexPolynomial multiply(ComplexPolynomial p) {
        Complex[] newFactors = new Complex[p.order() + this.order() + 1];

        Arrays.fill(newFactors, Complex.ZERO);

        for (int n1 = 0; n1 < p.factors.length; ++n1) {
            for (int n2 = 0; n2 < this.factors.length; ++n2) {
                newFactors[n1 + n2] = newFactors[n1 + n2].add(p.factors[n1].multiply(factors[n2]));
            }
        }

        return new ComplexPolynomial(newFactors);
    }

    public ComplexPolynomial derive() {
        Complex[] newFactors = new Complex[factors.length - 1];

        for (int n = 1; n < factors.length; ++n) {
            newFactors[n - 1] = factors[n].multiply(new Complex(n, 0));
        }

        return new ComplexPolynomial(newFactors);
    }

    public Complex apply(Complex z) {
        Complex result = Complex.ZERO;
        Complex zn = Complex.ONE; // z^n

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
