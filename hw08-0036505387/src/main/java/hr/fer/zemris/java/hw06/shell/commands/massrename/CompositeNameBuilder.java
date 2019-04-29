package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.util.Objects;

public class CompositeNameBuilder implements NameBuilder {

    private final NameBuilder[] nameBuilders;

    public CompositeNameBuilder(NameBuilder[] nameBuilders) {
        this.nameBuilders = Objects.requireNonNull(nameBuilders, "Cannot create a composite name builder of null.");
    }

    @Override
    public void execute(FilterResult result, StringBuilder sb) {
        for (NameBuilder nameBuilder : nameBuilders) {
            nameBuilder.execute(result, sb);
        }
    }
}
