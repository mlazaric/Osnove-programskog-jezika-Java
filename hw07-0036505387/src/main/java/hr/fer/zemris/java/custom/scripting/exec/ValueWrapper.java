package hr.fer.zemris.java.custom.scripting.exec;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;

public class ValueWrapper {

    private Object value;

    public ValueWrapper(Object value) {
        this.value = value;
    }

    private void checkType(Object value) {
        if (!(value == null || value instanceof Integer ||
                               value instanceof Double ||
                               value instanceof String)) {
            throw new RuntimeException("The value is not null, integer, double or string.");
        }
    }

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

    public void add(Object incValue) {
        value = performOperation(value, incValue, Integer::sum, Double::sum);
    }

    public void subtract(Object decValue) {
        value = performOperation(value, decValue,
                (i1, i2) -> i1 - i2,
                (d1, d2) -> d1 - d2);
    }

    public void multiply(Object mulValue) {
        value = performOperation(value, mulValue,
                (i1, i2) -> i1 * i2,
                (d1, d2) -> d1 * d2);
    }

    public void divide(Object divValue) {
        value = performOperation(value, divValue,
                (i1, i2) -> i1 / i2,
                (d1, d2) -> d1 / d2);
    }

    public int numCompare(Object withValue) {
        return performOperation(value, withValue, Integer::compare, Double::compare).intValue();
    }

    public Object getValue() {
        return value;
    }

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
