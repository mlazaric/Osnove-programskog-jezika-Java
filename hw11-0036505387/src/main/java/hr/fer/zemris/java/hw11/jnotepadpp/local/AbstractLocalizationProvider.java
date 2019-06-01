package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Models an abstract localization provider which implements adding listeners, removing listeners and firing the
 * localization change event to all subscribed listeners.
 *
 * @author Marko Lazarić
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    /**
     * The currently subscribed listeners.
     */
    private final List<ILocalizationListener> listeners;

    /**
     * Creates a new {@link AbstractLocalizationProvider}.
     */
    protected AbstractLocalizationProvider() {
        this.listeners = new ArrayList<>();
    }

    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all subscribed listeners of a localization change.
     */
    public void fire() {
        listeners.forEach(ILocalizationListener::localizationChanged);
    }

}
