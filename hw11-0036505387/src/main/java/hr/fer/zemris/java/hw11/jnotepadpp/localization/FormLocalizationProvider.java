package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class FormLocalizationProvider extends LocalizationProviderBridge {

    public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
        super(provider);

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                connect();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                disconnect();
            }

            @Override
            public void windowClosing(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }

}
