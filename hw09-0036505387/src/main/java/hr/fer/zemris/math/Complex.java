package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Complex {

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex ONE_NEG = new Complex(-1, 0);
    public static final Complex IM = new Complex(0, 1);
    public static final Complex IM_NEG = new Complex(0, -1);

    private final double real;
    private final double imag;

    public Complex() {
        this(0, 0);
    }

    public Complex(double re, double im) {
        this.real = re;
        this.imag = im;
    }

    public static Complex fromModuleAndArgument(double module, double argument) {
        double real = module * Math.cos(argument);
        double imag = module * Math.sin(argument);

        return new Complex(real, imag);
    }

    public double module() {
        return Math.sqrt(real * real + imag * imag);
    }

    public double argument() {
        return Math.atan2(imag, real);
    }

    public Complex multiply(Complex c) {
        Objects.requireNonNull(c, "Cannot multiply with null.");

        double re = this.real * c.real - this.imag * c.imag;
        double im = this.real * c.imag + this.imag * c.real;

        return new Complex(re, im);
    }

    public Complex divide(Complex c) {
        Objects.requireNonNull(c, "Cannot divide by null.");

        double re = this.real * c.imag + this.imag * c.real;
        double im = this.imag * c.real - this.real * c.imag;
        double mod = c.module();
        //http://mathworld.wolfram.com/ComplexDivision.html

        return new Complex(re / mod, im / mod);
    }

    public Complex add(Complex c) {
        Objects.requireNonNull(c, "Cannot add null.");

        return new Complex(real + c.real, imag + c.imag);
    }

    public Complex sub(Complex c) {
        Objects.requireNonNull(c, "Cannot subtract null.");

        return add(c.negate());
    }

    public Complex negate() {
        return multiply(ONE_NEG);
    }

    public Complex power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot calculate negative power.");
        }

        if (n == 0) {
            return ONE;
        }

        double argument = n * argument();
        double module = Math.pow(module(), n);

        return fromModuleAndArgument(module, argument);
    }

    public List<Complex> root(int n) {
        List<Complex> roots = new ArrayList<>();

        double argument = argument() / n;
        double module = Math.pow(module(), 1.0 / n);

        double argumentIncrement = 2 * Math.PI / n;

        for (int rootNumber = 0; rootNumber < n; ++rootNumber) {
            roots.add(fromModuleAndArgument(module, argument + argumentIncrement * rootNumber));
        }

        return roots;
    }

    @Override
    public String toString() {
        return String.format("(%.1f%+.1fi)", real, imag);
    }
}