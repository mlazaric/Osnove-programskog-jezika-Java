package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Objects;

/**
 * Models a localization provider which is able to connect to and disconnect from an existing localization provider.
 *
 * @author Marko Lazarić
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

    /**
     * Whether it is currently connected.
     */
    private boolean connected;

    /**
     * The provider to forward the localization requests to and to connect to.
     */
    private ILocalizationProvider provider;

    /**
     * The current localization language.
     */
    private String currentLanguage;

    /**
     * The listener to add to {@code provider} when connecting and to remove from it when disconnecting.
     */
    private ILocalizationListener listener = this::fire;

    /**
     * Creates a new {@link LocalizationProviderBridge} with the given arguments.
     *
     * @param provider the provider to forward the localization requests to
     *
     * @throws NullPointerException if the argument is null
     */
    public LocalizationProviderBridge(ILocalizationProvider provider) {
        this.provider = Objects.requireNonNull(provider, "Cannot use a null provider.");
    }

    @Override
    public String getString(String key) {
        return provider.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return provider.getCurrentLanguage();
    }

    /**
     * Connects to the localization provider.
     */
    public void connect() {
        if (connected) {
            return;
        }

        if (currentLanguage != null && currentLanguage != provider.getCurrentLanguage()) {
            fire();
        }

        connected = true;
        provider.addLocalizationListener(listener);
    }

    /**
     * Disconnects from the localization provider.
     */
    public void disconnect() {
        if (!connected) {
            return;
        }

        connected = false;
        provider.removeLocalizationListener(listener);
        currentLanguage = provider.getCurrentLanguage();
    }
}
