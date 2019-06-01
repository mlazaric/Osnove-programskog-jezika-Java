package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Models a complex polynomial represented by its list of roots and a scaling constant.
 *
 * @author Marko Lazarić
 */
public class ComplexRootedPolynomial {

    /**
     * The constant multiplying the polynomial.
     */
    private final Complex constant;

    /**
     * The list of the polynomial's roots.
     */
    private final Complex[] roots;

    /**
     * Creates a new {@link ComplexRootedPolynomial} with the given arguments.
     *
     * @param constant the constant multiplying the polynomial
     * @param roots the polynomial's roots
     *
     * @throws NullPointerException if either argument is {@code null}
     */
    public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
        this.constant = Objects.requireNonNull(constant, "Constant cannot be null.");
        this.roots = Objects.requireNonNull(roots, "Roots cannot be null.");
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

        Complex result = constant;

        for (Complex root : roots) {
            result = result.multiply(z.sub(root));
        }

        return result;
    }

    /**
     * Creates a {@link ComplexPolynomial} equal to this one.
     *
     * @return the equivalent {@link ComplexPolynomial}
     */
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

    /**
     * Returns the index of the root closest to {@code z} or -1 if the distance to the closest root is larger than threshold.
     *
     * @param z the point to find the closest root to
     * @param threshold how close the root should be
     * @return index of the closest root or -1
     *
     * @throws NullPointerException if {@code z} is {¢code null}
     */
    public int indexOfClosestRootFor(Complex z, double threshold) {
        Objects.requireNonNull(z, "Cannot find the index of closest root for null.");

        int index = 0;
        int minIndex = -1;
        double minDistance = threshold;

        for (Complex root : roots) {
            double distance = z.sub(root).module();

            if (distance < minDistance) {
                minDistance = distance;
                minIndex = index;
            }

            ++index;
        }

        return minIndex;
    }
}