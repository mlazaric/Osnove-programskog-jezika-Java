package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    private final List<ILocalizationListener> listeners;

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

    public void fire() {
        listeners.forEach(ILocalizationListener::localizationChanged);
    }

}
