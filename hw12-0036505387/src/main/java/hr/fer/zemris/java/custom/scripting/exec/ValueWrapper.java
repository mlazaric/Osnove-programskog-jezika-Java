package hr.fer.zemris.java.custom.scripting.exec;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;

/**
 * Wrapper for {@link Object} which implements certain operations if the {@link Object} is an {@link Integer},
 * {@link Double} or {@link String}.
 *
 * @author Marko Lazarić
 */
public class ValueWrapper {

    /**
     * The stored {@link Object}.
     */
    private Object value;

    /**
     * Creates a new {@link ValueWrapper} with the given arguments.
     *
     * @param value the value to store
     */
    public ValueWrapper(Object value) {
        this.value = value;
    }

    /**
     * Checks if the {@code value} is of a valid type. Valid types are: {@code null}, {@link Integer}, {@link Double}
     * and {@link String}. If it is none of those, it throws a {@link RuntimeException}.
     *
     * @param value the value whose type should be checked
     *
     * @throws RuntimeException if it is not of a valid type
     */
    private void checkType(Object value) {
        if (!(value == null || value instanceof Integer ||
                               value instanceof Double ||
                               value instanceof String)) {
            throw new RuntimeException("The value is not null, integer, double or string. " +
                    "Value is type of " + value.getClass() + ".");
        }
    }

    /**
     * Parses a string to either an {@link Integer} or a {@link Double}.
     *
     * If {@code toParse} is {@code null}, it returns 0.
     * Otherwise it first tries to parse it as {@link Integer}, then as {@link Double}
     * and if both parsings fail, it throws a {@link RuntimeException}.
     *
     * @param toParse the {@link String} to parse
     * @return the parsed number
     *
     * @throws RuntimeException if the string is not a valid {@link Integer} or {@link Double}
     */
    private Number parseString(String toParse) {
        if (toParse == null) {
            return 0;
        }

        try {
            return Integer.parseInt(toParse);
        } catch (NumberFormatException e) {}

        try {
            return Double.parseDouble(toParse);
        } catch (NumberFormatException e) {}

        throw new RuntimeException("'" + toParse + "' is not a valid number.");
    }

    /**
     * Performs an operation between two {@link Object}s.
     *
     * {@code null}s are turned into 0. Strings are parsed as either {@link Double} or {@link Integer}.
     *
     * If both numbers are integers, it performs the intOp and the result is an integer.
     * If either or both arguments are double, it performs the doubleOp and the result is a double.
     *
     * @param arg1 the first argument of the operation
     * @param arg2 the second argument of the operation
     * @param intOp the integer operation
     * @param doubleOp the double operation
     * @return the result of evaluating the operation with the given arguments
     *
     * @throws RuntimeException if one or both arguments do not represent a valid number
     *
     * @see #checkType(Object)
     * @see #parseString(String)
     */
    private Number performOperation(Object arg1, Object arg2, IntBinaryOperator intOp, DoubleBinaryOperator doubleOp) {
        checkType(arg1);
        checkType(arg2);

        if (arg1 instanceof String || arg1 == null) {
            arg1 = parseString((String) arg1);
        }
        if (arg2 instanceof String || arg2 == null) {
            arg2 = parseString((String) arg2);
        }

        Number num1 = (Number) arg1;
        Number num2 = (Number) arg2;

        if (arg1 instanceof Double || arg2 instanceof Double) {
            return doubleOp.applyAsDouble(num1.doubleValue(), num2.doubleValue());
        }
        else {
            return intOp.applyAsInt(num1.intValue(), num2.intValue());
        }
    }

    /**
     * Performs addition between the current value and the incValue. The result is stored in {@link #value}.
     *
     * @param incValue the value to add to {@link #value}
     */
    public void add(Object incValue) {
        value = performOperation(value, incValue, Integer::sum, Double::sum);
    }

    /**
     * Performs subtraction between the current value and the decValue. The result is stored in {@link #value}.
     *
     * @param decValue the value to subtract from {@link #value}
     */
    public void subtract(Object decValue) {
        value = performOperation(value, decValue,
                (i1, i2) -> i1 - i2,
                (d1, d2) -> d1 - d2);
    }

    /**
     * Performs multiplication between the current value and the mulValue. The result is stored in {@link #value}.
     *
     * @param mulValue the value to multiply {@link #value} with
     */
    public void multiply(Object mulValue) {
        value = performOperation(value, mulValue,
                (i1, i2) -> i1 * i2,
                (d1, d2) -> d1 * d2);
    }

    /**
     * Performs division between the current value and the divValue. The result is stored in {@link #value}.
     *
     * @param divValue the value to divide {@link #value} by
     */
    public void divide(Object divValue) {
        value = performOperation(value, divValue,
                (i1, i2) -> i1 / i2,
                (d1, d2) -> d1 / d2);
    }

    /**
     * Compares the current value with the withValue and returns the result.
     *
     * A negative return value means the first argument is less than the second one.
     * A zero means they are equal.
     * A positive return value means the first argument is greater than the second one.
     *
     * @param withValue the value to compare {@link #value} with
     * @return the result of the comparison
     */
    public int numCompare(Object withValue) {
        return performOperation(value, withValue, Integer::compare, Double::compare).intValue();
    }

    /**
     * Returns the current value.
     *
     * @return the current value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set {@link #value} to a new value.
     *
     * @param value the new value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ValueWrapper{value=" + value + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof ValueWrapper)) {
            return false;
        }

        ValueWrapper vw = (ValueWrapper) o;

        return Objects.equals(vw.value, this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
