package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Models an object which provides localization and allows subscription to localization change events.
 *
 * @author Marko Lazarić
 */
public interface ILocalizationProvider {

    /**
     * Returns the localized version of the key for the current language.
     *
     * @param key the key to localize
     * @return the localized version of the key for the current language
     */
    String getString(String key);

    /**
     * Adds a localization listener to this provider.
     *
     * @param listener the listener to add
     */
    void addLocalizationListener(ILocalizationListener listener);

    /**
     * Removes a localization listener from this provider.
     *
     * @param listener the listener to remove
     */
    void removeLocalizationListener(ILocalizationListener listener);

    /**
     * Returns the current language.
     *
     * @return the current language
     */
    String getCurrentLanguage();

}
