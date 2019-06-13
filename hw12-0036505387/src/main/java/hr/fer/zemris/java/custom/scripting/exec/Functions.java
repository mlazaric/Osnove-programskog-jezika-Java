package hr.fer.zemris.java.custom.scripting.exec;


import hr.fer.zemris.java.webserver.RequestContext;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Helper class for evaluating Smart Script functions.
 *
 * @author Marko Lazarić
 */
public final class Functions {

    private Functions() {} // Instances of this class should not be created.

    /**
     * The mapping of function names to functions.
     */
    private static final Map<String, BiConsumer<RequestContext, Stack<ValueWrapper>>> FUNCTIONS;

    static {
        FUNCTIONS = new HashMap<>();

        FUNCTIONS.put("sin", Functions::sin);
        FUNCTIONS.put("decfmt", Functions::decimalFormat);
        FUNCTIONS.put("dup", Functions::duplicate);
        FUNCTIONS.put("swap", Functions::swap);
        FUNCTIONS.put("setMimeType", Functions::setMimeType);

        FUNCTIONS.put("paramGet", paramGet(RequestContext::getParameter));
        FUNCTIONS.put("pparamGet", paramGet(RequestContext::getPersistentParameter));
        FUNCTIONS.put("tparamGet", paramGet(RequestContext::getTemporaryParameter));

        FUNCTIONS.put("pparamSet", paramSet(RequestContext::setPersistentParameter));
        FUNCTIONS.put("tparamSet", paramSet(RequestContext::setTemporaryParameter));

        FUNCTIONS.put("pparamDel", paramDel(RequestContext::removePersistentParameter));
        FUNCTIONS.put("tparamDel", paramDel(RequestContext::removeTemporaryParameter));
    }

    /**
     * Evaluates the specified function using the given context and stack.
     *
     * @param requestContext the context used for evaluating the function
     * @param stack the stack used for evaluating the function
     * @param name the name of the function
     *
     * @throws RuntimeException if {@code name} is not a valid function name
     */
    public static void applyFunction(RequestContext requestContext, Stack<ValueWrapper> stack, String name) {
        BiConsumer<RequestContext, Stack<ValueWrapper>> function = FUNCTIONS.get(name);

        if (function == null) {
            throw new RuntimeException("Invalid function name '" + name + "'.");
        }

        function.accept(requestContext, stack);
    }

    /**
     * Pops an element from the stack, calculates the sinus of that element (in degrees) and
     * pushes the result to the stack.
     *
     * @param requestContext the context used for evaluating the function
     * @param stack the stack used for evaluating the function
     */
    private static void sin(RequestContext requestContext, Stack<ValueWrapper> stack) {
        double x = getAsNumber(stack.pop()).doubleValue();

        double r = Math.sin(Math.toRadians(x));

        stack.push(new ValueWrapper(r));
    }

    /**
     * Pops two elements from the stack, formats the second argument using the first argument as {@link DecimalFormat}
     * and pushes the result to the stack.
     *
     * @param requestContext the context used for evaluating the function
     * @param stack the stack used for evaluating the function
     */
    private static void decimalFormat(RequestContext requestContext, Stack<ValueWrapper> stack) {
        String format = getAsString(stack.pop());
        double x = getAsNumber(stack.pop()).doubleValue();

        DecimalFormat decimalFormat = new DecimalFormat(format);

        String formattedNumber = decimalFormat.format(x);

        stack.push(new ValueWrapper(formattedNumber));
    }

    /**
     * Pops an element from the stack and pushes it to the stack twice.
     *
     * @param requestContext the context used for evaluating the function
     * @param stack the stack used for evaluating the function
     */
    private static void duplicate(RequestContext requestContext, Stack<ValueWrapper> stack) {
        ValueWrapper wrapper = stack.pop();

        stack.push(wrapper);
        stack.push(new ValueWrapper(wrapper.getValue())); // Prevent mutations
    }

    /**
     * Pops two elements from the stack, swaps their places and pushes them to the stack.
     *
     * @param requestContext the context used for evaluating the function
     * @param stack the stack used for evaluating the function
     */
    private static void swap(RequestContext requestContext, Stack<ValueWrapper> stack) {
        ValueWrapper wrapper1 = stack.pop();
        ValueWrapper wrapper2 = stack.pop();

        stack.push(wrapper1);
        stack.push(wrapper2);
    }

    /**
     * Pops an element from the stack and sets the mime type to its value.
     *
     * @param requestContext the context used for evaluating the function
     * @param stack the stack used for evaluating the function
     */
    private static void setMimeType(RequestContext requestContext, Stack<ValueWrapper> stack) {
        String mimeType = getAsString(stack.pop());

        requestContext.setMimeType(mimeType);
    }

    /**
     * Factory method for creating paramGet/tparamGet/pparamGet functions.
     *
     * @param paramType the function to use to extract the parameter
     * @return a function which pops two elements from the stack, extracts the parameter with the given name and pushes
     *          its value or the default value to the stack
     */
    private static BiConsumer<RequestContext, Stack<ValueWrapper>> paramGet(BiFunction<RequestContext, String, String> paramType) {
        return (requestContext, stack) -> {
            ValueWrapper defaultValue = stack.pop();
            String name = getAsString(stack.pop());

            String param = paramType.apply(requestContext, name);

            if (param == null) {
                stack.push(defaultValue);
            }
            else {
                stack.push(new ValueWrapper(param));
            }
        };
    }

    /**
     * Factory method for creating tparamSet/pparamSet functions.
     *
     * @param paramType the function to use to set the parameter
     * @return a function which pops two elements from the stack and sets the parameter with the given name to the given
     *         value
     */
    private static BiConsumer<RequestContext, Stack<ValueWrapper>> paramSet(TriConsumer<RequestContext, String, String> paramType) {
        return (requestContext, stack) -> {
            String name = getAsString(stack.pop());
            String value = getAsString(stack.pop());

            paramType.accept(requestContext, name, value);
        };
    }

    /**
     * Factory method for creating tparamDel/pparamDel functions.
     *
     * @param paramType the function to use to delete the parameter
     * @return a function which pops one element from the stack and deletes the parameter with the given name
     */
    private static BiConsumer<RequestContext, Stack<ValueWrapper>> paramDel(BiConsumer<RequestContext, String> paramType) {
        return (requestContext, stack) -> {
            String name = getAsString(stack.pop());

            paramType.accept(requestContext, name);
        };
    }

    /**
     * Extract a {@link Number} from a {@link ValueWrapper} or throw exception.
     *
     * @param wrapper the {@link ValueWrapper} whose value should be extracted as a {@link Number}
     * @return the extracted {@link Number}
     *
     * @throws RuntimeException if the value is not a {@link Number}, nor a parsable {@link String}
     */
    private static Number getAsNumber(ValueWrapper wrapper) {
        Object value = wrapper.getValue();

        if (value instanceof Number) {
            return (Number) value;
        }
        else if (value instanceof String) {
            return Double.valueOf((String) value);
        }

        throw new RuntimeException("'" + value + "' is not a number.");
    }

    /**
     * Extract a {@link String} from a {@link ValueWrapper}.
     *
     * @param wrapper the {@link ValueWrapper} whose value should be extracted as a {@link String}
     * @return the extracted {@link String}
     */
    private static String getAsString(ValueWrapper wrapper) {
        Object value = wrapper.getValue();

        if (value instanceof String) {
            return (String) value;
        }
        else {
            return String.valueOf(value);
        }
    }
}
