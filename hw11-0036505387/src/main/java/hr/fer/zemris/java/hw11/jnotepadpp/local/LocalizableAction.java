package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.*;
import java.util.Objects;

/**
 * Models a {@link Action} with localized name and optional localized description.
 *
 * @author Marko Lazarić
 */
public abstract class LocalizableAction extends AbstractAction {

    /**
     * The suffix used for description localization.
     */
    private static final String DESCRIPTION_SUFFIX = ".description";

    /**
     * The localization key to use for the name.
     */
    private final String key;

    /**
     * The localization provider to use.
     */
    private final ILocalizationProvider provider;

    /**
     * Creates a new {@link LocalizableAction} with the given arguments.
     *
     * @param key the localization key to use for the text
     * @param provider the localization provider to use
     *
     * @throws NullPointerException if either argument is null
     */
    public LocalizableAction(String key, ILocalizationProvider provider) {
        this.key = Objects.requireNonNull(key, "Localization key cannot be null.");
        this.provider = Objects.requireNonNull(provider, "Localization provider cannot be null.");

        setName();

        provider.addLocalizationListener(this::setName);
    }

    /**
     * Updates the name of the {@link Action} to the localized version and optionally updates the description to the
     * localized version if it is defined.
     */
    private void setName() {
        putValue(NAME, provider.getString(key));

        try {
            putValue(SHORT_DESCRIPTION, provider.getString(key + DESCRIPTION_SUFFIX));
        }
        catch (Exception ignored) {} // The description is optional
    }

}
