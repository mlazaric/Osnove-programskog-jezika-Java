package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.util.Objects;

/**
 * Models a simple composite name builder.
 *
 * @author Marko Lazarić
 */
public class CompositeNameBuilder implements NameBuilder {

    /**
     * The name builders this encompasses.
     */
    private final NameBuilder[] nameBuilders;

    /**
     * Creates a new {@link CompositeNameBuilder} with the given arguments.
     *
     * @param nameBuilders the name builders this encompasses
     *
     * @throws NullPointerException if {@code nameBuilders} is {@code null}
     */
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
