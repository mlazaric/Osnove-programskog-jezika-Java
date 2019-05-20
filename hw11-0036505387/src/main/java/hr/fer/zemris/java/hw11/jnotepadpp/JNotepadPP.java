package hr.fer.zemris.java.hw11.jnotepadpp;

import hr.fer.zemris.java.hw11.jnotepadpp.document.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.localization.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.localization.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.localization.LocalizationProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JNotepadPP extends JFrame {

    private DefaultMultipleDocumentModel tabs;
    private FormLocalizationProvider provider;

    public JNotepadPP() {
        provider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

        setSize(500, 500);

        initGUI();
    }

    private void initGUI() {
        tabs = new DefaultMultipleDocumentModel();

        add(tabs, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JNotepadPP().setVisible(true);
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private final LocalizableAction createBlankDocument = new LocalizableAction("create_blank_document", provider) {

        @Override
        public void actionPerformed(ActionEvent e) {
            tabs.createNewDocument();
        }

    };

    private final LocalizableAction openExistingDocument = new LocalizableAction("open_existing_document", provider) {

        @Override
        public void actionPerformed(ActionEvent e) {
            openExistingDocument();
        }

    };

    private void openExistingDocument() {
        JFileChooser jfc = new JFileChooser();

        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                tabs.loadDocument(jfc.getSelectedFile().toPath());
            }
            catch (RuntimeException e) {
                showError(e.getMessage());
            }
        }
    }



    private final LocalizableAction saveDocument = new LocalizableAction("save_document", provider) {

        @Override
        public void actionPerformed(ActionEvent e) {
            saveDocument();
        }

    };

    private void saveDocument() {
        try {
            tabs.saveDocument(tabs.getCurrentDocument(), null);
        }
        catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private final LocalizableAction saveAsDocument = new LocalizableAction("save_as_document", provider) {

        @Override
        public void actionPerformed(ActionEvent e) {
            saveAsDocument();
        }

    };

    private void saveAsDocument() {
        JFileChooser jfc = new JFileChooser();

        if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                tabs.saveDocument(tabs.getCurrentDocument(), jfc.getSelectedFile().toPath());
            }
            catch (RuntimeException e) {
                showError(e.getMessage());
            }
        }
    }

    private final LocalizableAction closeDocument = new LocalizableAction("close_document", provider) {

        @Override
        public void actionPerformed(ActionEvent e) {
            closeDocument();
        }

    };

    private void closeDocument() {
        SingleDocumentModel document = tabs.getCurrentDocument();

        if (document == null) {
            return;
        }

        if (document.isModified()) {
            saveAsDocument();
        }

        if (!document.isModified()) {
            tabs.closeDocument(document);
        }
    }


}
