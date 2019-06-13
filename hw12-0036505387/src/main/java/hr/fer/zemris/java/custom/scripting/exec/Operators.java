package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Helper class for evaluating Smart Script operators.
 *
 * @author Marko Lazarić
 */
public final class Operators {

    private Operators() {} // Instances of this class should not be created.

    /**
     * The mapping of operator symbols to operators.
     */
    private static final Map<String, BiConsumer<ValueWrapper, Object>> OPERATORS;

    static {
        OPERATORS = new HashMap<>();

        OPERATORS.put("+", ValueWrapper::add);
        OPERATORS.put("-", ValueWrapper::subtract);
        OPERATORS.put("*", ValueWrapper::multiply);
        OPERATORS.put("/", ValueWrapper::divide);
    }

    /**
     * Evaluates the specified operator using the given arguments.
     *
     * @param symbol the symbol of the operator
     * @param first the first argument for the operator
     * @param second the second argument for the operator
     * @return the result of evaluating the operator
     *
     * @throws RuntimeException if {@code symbol} is not a valid Smart Script operator
     */
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
