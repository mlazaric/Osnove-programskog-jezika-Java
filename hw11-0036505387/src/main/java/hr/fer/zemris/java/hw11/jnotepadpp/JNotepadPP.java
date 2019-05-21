package hr.fer.zemris.java.hw11.jnotepadpp;

import hr.fer.zemris.java.hw11.jnotepadpp.document.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.localization.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class JNotepadPP extends JFrame {

    private DefaultMultipleDocumentModel tabs;
    private FormLocalizationProvider provider;

    private Action createBlankDocument;
    private Action openExistingDocument;
    private Action saveDocument;
    private Action saveAsDocument;
    private Action closeDocument;
    private Action languageEn;
    private Action languageHr;

    public JNotepadPP() {
        provider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

        setSize(500, 500);

        initGUI();
    }

    private void initGUI() {
        tabs = new DefaultMultipleDocumentModel();

        add(tabs, BorderLayout.CENTER);

        initActions();
        createMenus();
    }

    private void initActions() {
        createBlankDocument = createAction("create_blank_document", provider, tabs::createNewDocument);
        openExistingDocument = createAction("open_existing_document", provider, this::openExistingDocument);
        saveDocument = createAction("save_document", provider, this::saveDocument);
        saveAsDocument = createAction("save_as_document", provider, this::saveAsDocument);
        closeDocument = createAction("close_document", provider, this::closeDocument);

        languageEn = createAction("language_en", provider, () -> LocalizationProvider.getInstance().setLanguage("en"));
        languageHr = createAction("language_hr", provider, () -> LocalizationProvider.getInstance().setLanguage("hr"));

        setKeys(createBlankDocument, KeyStroke.getKeyStroke("control N"), KeyEvent.VK_N);
        setKeys(openExistingDocument, KeyStroke.getKeyStroke("control O"), KeyEvent.VK_O);
        setKeys(saveDocument, KeyStroke.getKeyStroke("control S"), KeyEvent.VK_S);
        setKeys(saveAsDocument, KeyStroke.getKeyStroke("control shift S"), KeyEvent.VK_A);
        setKeys(closeDocument, KeyStroke.getKeyStroke("control X"), KeyEvent.VK_X);

        // Should only be enabled if the current document is modified and has path set
        saveDocument.setEnabled(false);
        tabs.addMultipleDocumentListener(new MultipleDocumentListener() {
            private void updateSaveDocument(SingleDocumentModel currentModel) {
                saveDocument.setEnabled(currentModel != null &&
                                        currentModel.isModified() &&
                                        currentModel.getFilePath() != null);
            }

            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                updateSaveDocument(currentModel);
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                model.addSingleDocumentListener(new SingleDocumentListener() {

                    @Override
                    public void documentModifyStatusUpdated(SingleDocumentModel model) {
                        updateSaveDocument(model);
                    }

                    @Override
                    public void documentFilePathUpdated(SingleDocumentModel model) {
                        updateSaveDocument(model);
                    }

                });
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {}
        });

        // Should only be enabled if the current document is modified
        saveAsDocument.setEnabled(false);
        tabs.addMultipleDocumentListener(new MultipleDocumentListener() {
            private void updateSaveAsDocument(SingleDocumentModel currentModel) {
                saveAsDocument.setEnabled(currentModel != null &&
                                          currentModel.isModified());
            }

            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                updateSaveAsDocument(currentModel);
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                model.addSingleDocumentListener(new SingleDocumentListener() {

                    @Override
                    public void documentModifyStatusUpdated(SingleDocumentModel model) {
                        updateSaveAsDocument(model);
                    }

                    @Override
                    public void documentFilePathUpdated(SingleDocumentModel model) {
                        updateSaveAsDocument(model);
                    }

                });
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {}
        });

        // Should only be enabled if there are any documents open
        closeDocument.setEnabled(false);
        tabs.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {}

            @Override
            public void documentAdded(SingleDocumentModel model) {
                closeDocument.setEnabled(true);
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                if (tabs.getNumberOfDocuments() == 0) {
                    closeDocument.setEnabled(false);
                }
            }
        });
    }

    private void setKeys(Action action, KeyStroke keyStroke, int keyEvent) {
        action.putValue(Action.ACCELERATOR_KEY, keyStroke);
        action.putValue(Action.MNEMONIC_KEY, keyEvent);
    }

    private Action createAction(String key, ILocalizationProvider provider, Runnable runnable) {
        return new LocalizableAction(key, provider) {

            @Override
            public void actionPerformed(ActionEvent e) {
                runnable.run();
            }

        };
    }

    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new LocalizableJMenu("file", provider);

        menuBar.add(file);

        file.add(createBlankDocument);
        file.add(openExistingDocument);
        file.add(saveDocument);
        file.add(saveAsDocument);
        file.add(closeDocument);

        JMenu languages = new LocalizableJMenu("localization", provider);

        menuBar.add(languages);

        languages.add(languageEn);
        languages.add(languageHr);

        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JNotepadPP().setVisible(true);
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

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

    private void saveDocument() {
        try {
            tabs.saveDocument(tabs.getCurrentDocument(), null);
        }
        catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

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
