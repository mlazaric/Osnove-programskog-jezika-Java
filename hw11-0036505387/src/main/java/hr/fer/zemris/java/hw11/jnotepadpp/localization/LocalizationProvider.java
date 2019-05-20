package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {

    private static final LocalizationProvider instance = new LocalizationProvider();

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
        return instance;
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }
}
