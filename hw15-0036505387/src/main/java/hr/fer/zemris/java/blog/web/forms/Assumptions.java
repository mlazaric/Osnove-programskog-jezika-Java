package hr.fer.zemris.java.blog.web.forms;

import java.util.Objects;

/**
 * An object used for validating the {@link String} values of attributes based on certain assumptions.
 *
 * @author Marko Lazarić
 */
public class Assumptions {

    /**
     * The name of the attribute.
     */
    private final String name;

    /**
     * The value of the attribute to validate.
     */
    private final String value;

    /**
     * The optional error found while validating the value of the attribute.
     */
    private String error;

    /**
     * Creates a new {@link Assumptions} with the given arguments.
     *
     * @param name the name of the attribute
     * @param value the value of the attribute to validate
     *
     * @throws NullPointerException if {@code name} is null
     */
    public Assumptions(String name, String value) {
        this.name = Objects.requireNonNull(name, "Name of attribute cannot be null.");
        this.value = value != null ? value : "";
    }

    /**
     * Returns whether it has encountered a failed assumption while validating the value of the attribute.
     *
     * @return true if it has encountered a failed assumption, false otherwise
     */
    public boolean hasError() {
        return error != null;
    }

    /**
     * Returns the error message for the encountered failed assumption or null if all assumptions have passed.
     *
     * @return the error message for the encountered failed assumption or null if all assumptions have passed
     */
    public String getError() {
        return error;
    }

    /**
     * Assumes the value is required and it should not be blank.
     */
    public Assumptions isRequired() {
        if (error != null) {
            return this;
        }

        if (value.isBlank()) {
            error = name + " is required.";
        }

        return this;
    }

    /**
     * Assumes the value represents a parsable {@link Long}.
     */
    public Assumptions isParsableLong() {
        if (error != null) {
            return this;
        }

        try {
            Long.parseLong(value);
        }
        catch (NumberFormatException e) {
            error = "The value of " + name + " is not valid.";
        }

        return this;
    }

    /**
     * Assumes the value represents a valid email address.
     */
    public Assumptions isValidEmailAddress() {
        if (error != null) {
            return this;
        }

        int length = value.length();
        int indexOfAt = value.indexOf('@');

        if (length < 3 || indexOfAt == -1 || indexOfAt == 0 || indexOfAt == length - 1) {
            error = "The value of " + name + " is not a valid email address.";
        }

        return this;
    }
}
