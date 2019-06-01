package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Models a listener object that should be notified every time localization changes.
 *
 * @author Marko Lazarić
 */
@FunctionalInterface
public interface ILocalizationListener {

    /**
     * Method called every time localization changes.
     */
    void localizationChanged();

}
