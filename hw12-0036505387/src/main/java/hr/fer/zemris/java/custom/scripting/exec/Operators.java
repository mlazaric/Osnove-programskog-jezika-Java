package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class Operators {

    private Operators() {}

    private static final Map<String, BiConsumer<ValueWrapper, Object>> OPERATORS;

    static {
        OPERATORS = new HashMap<>();

        OPERATORS.put("+", ValueWrapper::add);
        OPERATORS.put("-", ValueWrapper::subtract);
        OPERATORS.put("*", ValueWrapper::multiply);
        OPERATORS.put("/", ValueWrapper::divide);
    }

    public static ValueWrapper applyOperator(String symbol, ValueWrapper first, ValueWrapper second) {
        BiConsumer<ValueWrapper, Object> operator = OPERATORS.get(symbol);

        if (operator == null) {
            throw new RuntimeException("Invalid operator symbol '" + symbol + "'.");
        }

        ValueWrapper result = new ValueWrapper(first.getValue()); // So we do not mutate the argument

        operator.accept(result, second.getValue());

        return result;
    }

}
