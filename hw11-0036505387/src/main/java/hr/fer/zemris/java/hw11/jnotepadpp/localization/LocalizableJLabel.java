package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import javax.swing.*;
import java.util.Objects;

public class LocalizableJLabel extends JLabel {

    private final String key;
    private final ILocalizationProvider provider;

    public LocalizableJLabel(String key, ILocalizationProvider provider) {
        this.key = Objects.requireNonNull(key, "Localization key cannot be null.");
        this.provider = Objects.requireNonNull(provider, "Localization provider cannot be null.");

        updateText();

        provider.addLocalizationListener(this::updateText);
    }

    private void updateText() {
        setText(provider.getString(key));
    }

}
