package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * Models a localization provider bridge which is tied to a {@link JFrame}.
 * It connects when the {@link JFrame} is opened and disconnects when it is closed.
 *
 * @author Marko Lazarić
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

    /**
     * Creates a new {@link FormLocalizationProvider} using provider which is tied to frame.
     *
     * @param provider the provider to forward localization requests to
     * @param frame the frame it is tied to
     *
     * @throws NullPointerException if either argument is null
     */
    public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
        super(provider);

        Objects.requireNonNull(frame, "Frame cannot be null.");

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                connect();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                disconnect();
            }
        });
    }

}
