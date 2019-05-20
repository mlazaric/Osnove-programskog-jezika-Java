package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import java.util.Objects;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {

    private boolean connected;
    private ILocalizationProvider provider;

    private ILocalizationListener listener = this::fire;

    public LocalizationProviderBridge(ILocalizationProvider provider) {
        this.provider = Objects.requireNonNull(provider, "Cannot use a null provider.");
    }

    @Override
    public String getString(String key) {
        return provider.getString(key);
    }

    public void connect() {
        if (connected) {
            return;
        }

        connected = true;
        provider.addLocalizationListener(listener);
    }

    public void disconnect() {
        if (!connected) {
            return;
        }

        connected = false;
        provider.removeLocalizationListener(listener);
    }
}
