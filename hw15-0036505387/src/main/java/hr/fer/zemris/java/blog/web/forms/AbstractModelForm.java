package hr.fer.zemris.java.blog.web.forms;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractModelForm<E> implements ModelForm<E> {

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

    public Map<String, String> getErrors() {
        return errors;
    }

}
