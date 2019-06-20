package hr.fer.zemris.java.tecaj_13.web.forms;

import java.util.Objects;

public class Assumptions {

    private final String name;
    private final String value;

    private String error;

    public Assumptions(String name, String value) {
        this.name = Objects.requireNonNull(name, "Name of attribute cannot be null.");
        this.value = value != null ? value : "";
    }

    public boolean hasError() {
        return error != null;
    }

    public String getError() {
        return error;
    }

    public Assumptions isRequired() {
        if (error != null) {
            return this;
        }

        if (value.isBlank()) {
            error = name + " is required.";
        }

        return this;
    }

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
