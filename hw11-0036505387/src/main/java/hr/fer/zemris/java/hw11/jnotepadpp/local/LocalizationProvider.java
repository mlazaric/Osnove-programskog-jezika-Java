package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Models a singleton localization provider using {@link ResourceBundle}.
 *
 * @author Marko Lazarić
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    /**
     * The singleton instance.
     */
    private static LocalizationProvider instance;

    /**
     * The current language.
     */
    private String language;

    /**
     * The currently loaded bundle.
     */
    private ResourceBundle bundle;

    /**
     * Creates a new {@link LocalizationProvider} with English as its default language.
     */
    private LocalizationProvider() {
        language = "en";
        loadBundle();
    }

    /**
     * Loads the {@link ResourceBundle}.
     */
    private void loadBundle() {
        Locale locale = Locale.forLanguageTag(language);

        bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp", locale);
    }

    /**
     * Returns the singleton instance.
     *
     * @return the singleton instance
     */
    public static LocalizationProvider getInstance() {
        if (instance == null) {
            instance = new LocalizationProvider();
        }

        return instance;
    }

    /**
     * Sets the current language to the argument and notifies listeners.
     *
     * @param language the new language to use for localization
     */
    public void setLanguage(String language)  {
        this.language = language;

        loadBundle();
        fire();
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return language;
    }
}
