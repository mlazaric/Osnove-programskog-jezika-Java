package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.*;
import java.util.Objects;

/**
 * Models a {@link JLabel} with localized text.
 *
 * @author Marko Lazarić
 */
public class LocalizableJLabel extends JLabel {

    /**
     * The localization key to use for the text.
     */
    private final String key;

    /**
     * The localization provider to use.
     */
    private final ILocalizationProvider provider;

    /**
     * Creates a new {@link LocalizableJLabel} with the given arguments.
     *
     * @param key the localization key to use for the text
     * @param provider the localization provider to use
     *
     * @throws NullPointerException if either argument is null
     */
    public LocalizableJLabel(String key, ILocalizationProvider provider) {
        this.key = Objects.requireNonNull(key, "Localization key cannot be null.");
        this.provider = Objects.requireNonNull(provider, "Localization provider cannot be null.");

        updateText();

        provider.addLocalizationListener(this::updateText);
    }

    /**
     * Updates the text of the {@link JLabel} to the localized version.
     */
    private void updateText() {
        setText(provider.getString(key));
    }

}
