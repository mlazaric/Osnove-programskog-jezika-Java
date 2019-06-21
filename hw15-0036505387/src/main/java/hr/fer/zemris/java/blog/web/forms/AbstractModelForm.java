package hr.fer.zemris.java.blog.web.forms;

import java.util.HashMap;
import java.util.Map;

/**
 * Models an abstract {@link ModelForm} which implements the error storage and error methods.
 *
 * @param <E> the type of the entity represented by this form
 */
public abstract class AbstractModelForm<E> implements ModelForm<E> {

    /**
     * The errors found while validating the values of the attributes.
     * The key is the attribute name, the value is the error message for the attribute value.
     */
    protected final Map<String, String> errors = new HashMap<>();

    @Override
    public boolean hasAnyErrors() {
        return !errors.isEmpty();
    }

    @Override
    public boolean hasErrorForAttribute(String attribute) {
        return errors.containsKey(attribute);
    }

    @Override
    public String getErrorForAttribute(String attribute) {
        return errors.get(attribute);
    }

    /**
     * Returns the {@link #errors} map, used for iterating over it to display the messages.
     *
     * @return the {@link #errors} map
     */
    public Map<String, String> getErrors() {
        return errors;
    }

    /**
     * Normalises a {@link String} attribute value.
     * A null is turned into an empty {@link String} "" and any {@link String} is trimmed.
     *
     * @param value the value to normalise
     * @return the normalised value
     */
    protected String normalise(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }

    /**
     * Checks the given {@link Assumptions}, if it has any errors, it adds them to the {@link #errors} map.
     *
     * @param name the name of the attribute
     * @param assumptions the assumptions on the attribute value
     */
    protected void checkAssumptions(String name, Assumptions assumptions) {
        if (assumptions.hasError()) {
            errors.put(name, assumptions.getError());
        }
    }

}
