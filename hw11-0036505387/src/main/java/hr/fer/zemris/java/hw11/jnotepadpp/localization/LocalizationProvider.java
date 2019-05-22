package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {

    private static LocalizationProvider instance;

    private String language;
    private ResourceBundle bundle;

    private LocalizationProvider() {
        language = "en";
        loadBundle();
    }

    private void loadBundle() {
        Locale locale = Locale.forLanguageTag(language);

        bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp", locale);
    }

    public static LocalizationProvider getInstance() {
        if (instance == null) {
            instance = new LocalizationProvider();
        }

        return instance;
    }

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
