package hr.fer.zemris.java.custom.scripting.exec;


import hr.fer.zemris.java.webserver.RequestContext;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public final class Functions {

    private Functions() {}

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

    public static void applyFunction(RequestContext requestContext, Stack<ValueWrapper> stack, String name) {
        BiConsumer<RequestContext, Stack<ValueWrapper>> function = FUNCTIONS.get(name);

        if (function == null) {
            throw new RuntimeException("Invalid function name '" + name + "'.");
        }

        function.accept(requestContext, stack);
    }

    private static void sin(RequestContext requestContext, Stack<ValueWrapper> stack) {
        double x = getAsNumber(stack.pop()).doubleValue();

        double r = Math.sin(Math.toRadians(x));

        stack.push(new ValueWrapper(r));
    }

    private static void decimalFormat(RequestContext requestContext, Stack<ValueWrapper> stack) {
        String format = getAsString(stack.pop());
        double x = getAsNumber(stack.pop()).doubleValue();

        DecimalFormat decimalFormat = new DecimalFormat(format);

        String formattedNumber = decimalFormat.format(x);

        stack.push(new ValueWrapper(formattedNumber));
    }

    private static void duplicate(RequestContext requestContext, Stack<ValueWrapper> stack) {
        ValueWrapper wrapper = stack.pop();

        stack.push(wrapper);
        stack.push(wrapper);
    }

    private static void swap(RequestContext requestContext, Stack<ValueWrapper> stack) {
        ValueWrapper wrapper1 = stack.pop();
        ValueWrapper wrapper2 = stack.pop();

        stack.push(wrapper1);
        stack.push(wrapper2);
    }

    private static void setMimeType(RequestContext requestContext, Stack<ValueWrapper> stack) {
        String mimeType = getAsString(stack.pop());

        requestContext.setMimeType(mimeType);
    }

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

    private static BiConsumer<RequestContext, Stack<ValueWrapper>> paramSet(TriConsumer<RequestContext, String, String> paramType) {
        return (requestContext, stack) -> {
            String name = getAsString(stack.pop());
            String value = getAsString(stack.pop());

            paramType.accept(requestContext, name, value);
        };
    }

    private static BiConsumer<RequestContext, Stack<ValueWrapper>> paramDel(BiConsumer<RequestContext, String> paramType) {
        return (requestContext, stack) -> {
            String name = getAsString(stack.pop());

            paramType.accept(requestContext, name);
        };
    }

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
