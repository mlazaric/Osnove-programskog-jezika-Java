package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import javax.swing.*;
import java.util.Objects;

public abstract class LocalizableAction extends AbstractAction {

    private static final String DESCRIPTION_SUFFIX = ".description";

    private final String key;
    private final ILocalizationProvider provider;

    public LocalizableAction(String key, ILocalizationProvider provider) {
        this.key = Objects.requireNonNull(key, "Localization key cannot be null.");
        this.provider = Objects.requireNonNull(provider, "Localization provider cannot be null.");

        setName();

        provider.addLocalizationListener(this::setName);
    }

    private void setName() {
        putValue(NAME, provider.getString(key));

        try {
            putValue(SHORT_DESCRIPTION, provider.getString(key + DESCRIPTION_SUFFIX));
        }
        catch (Exception ignored) {} // The description is optional
    }

}
