package hr.fer.zemris.math;

import java.util.Objects;

public class ComplexRootedPolynomial {

    private final Complex constant;
    private final Complex[] roots;

    public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
        this.constant = Objects.requireNonNull(constant, "Constant cannot be null.");
        this.roots = Objects.requireNonNull(roots, "Roots cannot be null.");
    }

    public Complex apply(Complex z) {
        Objects.requireNonNull(z, "Cannot apply polynomial to null.");

        Complex result = constant;

        for (Complex root : roots) {
            result = result.multiply(z.subtract(root));
        }

        return result;
    }

    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial result = new ComplexPolynomial(new Complex[]{ constant });

        for (Complex root : roots) {
            ComplexPolynomial part = new ComplexPolynomial(new Complex[]{ root.negate(), Complex.ONE });

            result = result.multiply(part);
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(constant);

        for (Complex root : roots) {
            sb.append("*(z-");
            sb.append(root);
            sb.append(")");
        }

        return sb.toString();
    }

    public int indexOfClosestRootFor(Complex z, double treshold) {
        int index = 0;
        int minIndex = -1;
        double minDistance = treshold;

        for (Complex root : roots) {
            double distance = z.subtract(root).module();

            if (distance < minDistance) {
                minDistance = distance;
                minIndex = index;
            }

            ++index;
        }

        return minIndex;
    }
}