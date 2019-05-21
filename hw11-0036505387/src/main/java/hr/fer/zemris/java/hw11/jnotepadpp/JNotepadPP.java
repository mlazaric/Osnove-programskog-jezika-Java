package hr.fer.zemris.java.hw11.jnotepadpp;

import hr.fer.zemris.java.hw11.jnotepadpp.document.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.localization.*;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.EditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
    private Action cutText;
    private Action copyText;
    private Action pasteText;

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
        createBlankDocument = createAction("create_blank_document", tabs::createNewDocument);
        openExistingDocument = createAction("open_existing_document", this::openExistingDocument);
        saveDocument = createAction("save_document", this::saveDocument);
        saveAsDocument = createAction("save_as_document", this::saveAsDocument);
        closeDocument = createAction("close_document", this::closeDocument);

        cutText = createAction("cut_text", new DefaultEditorKit.CutAction());
        copyText = createAction("copy_text", new DefaultEditorKit.CopyAction());
        pasteText = createAction("paste_text", new DefaultEditorKit.PasteAction());

        languageEn = createAction("language_en", () -> LocalizationProvider.getInstance().setLanguage("en"));
        languageHr = createAction("language_hr", () -> LocalizationProvider.getInstance().setLanguage("hr"));

        setKeys(createBlankDocument, KeyStroke.getKeyStroke("control N"), KeyEvent.VK_N);
        setKeys(openExistingDocument, KeyStroke.getKeyStroke("control O"), KeyEvent.VK_O);
        setKeys(saveDocument, KeyStroke.getKeyStroke("control S"), KeyEvent.VK_S);
        setKeys(saveAsDocument, KeyStroke.getKeyStroke("control shift S"), KeyEvent.VK_A);
        setKeys(closeDocument, KeyStroke.getKeyStroke("control W"), KeyEvent.VK_C);

        setKeys(cutText, KeyStroke.getKeyStroke("control X"), KeyEvent.VK_U);
        setKeys(copyText, KeyStroke.getKeyStroke("control C"), KeyEvent.VK_C);
        setKeys(pasteText, KeyStroke.getKeyStroke("control V"), KeyEvent.VK_P);

        // Should only be enabled if the current document is modified and has path set
        saveDocument.setEnabled(false);
        conditionallyEnable(saveDocument, model -> model != null &&
                                                   model.isModified() &&
                                                   model.getFilePath() != null);

        // Should only be enabled if the current document is modified
        saveAsDocument.setEnabled(false);
        conditionallyEnable(saveAsDocument, model -> model != null && model.isModified());

        // Should only be enabled if there is a documents open
        closeDocument.setEnabled(false);
        conditionallyEnable(closeDocument, () -> tabs.getCurrentDocument() != null);

        // Should only be enabled if there is a document open
        cutText.setEnabled(false);
        copyText.setEnabled(false);
        pasteText.setEnabled(false);
        conditionallyEnable(cutText, () -> tabs.getCurrentDocument() != null);
        conditionallyEnable(copyText, () -> tabs.getCurrentDocument() != null);
        conditionallyEnable(pasteText, () -> tabs.getCurrentDocument() != null);

    }

    private void conditionallyEnable(Action action, Predicate<SingleDocumentModel> shouldEnable) {
        tabs.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                action.setEnabled(shouldEnable.test(currentModel));
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                model.addSingleDocumentListener(new SingleDocumentListener() {

                    @Override
                    public void documentModifyStatusUpdated(SingleDocumentModel model) {
                        action.setEnabled(shouldEnable.test(model));
                    }

                    @Override
                    public void documentFilePathUpdated(SingleDocumentModel model) {
                        action.setEnabled(shouldEnable.test(model));
                    }

                });
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {}
        });
    }

    private void conditionallyEnable(Action action, Supplier<Boolean> shouldEnable) {
        tabs.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {}

            @Override
            public void documentAdded(SingleDocumentModel model) {
                action.setEnabled(shouldEnable.get());
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                action.setEnabled(shouldEnable.get());
            }
        });
    }

    private void setKeys(Action action, KeyStroke keyStroke, int keyEvent) {
        action.putValue(Action.ACCELERATOR_KEY, keyStroke);
        action.putValue(Action.MNEMONIC_KEY, keyEvent);
    }

    private Action createAction(String key, Runnable runnable) {
        return new LocalizableAction(key, provider) {

            @Override
            public void actionPerformed(ActionEvent e) {
                runnable.run();
            }

        };
    }

    private Action createAction(String key, Action action) {
        return createAction(key, () -> action.actionPerformed(null));
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

        JMenu edit = new LocalizableJMenu("edit", provider);

        menuBar.add(edit);

        edit.add(cutText);
        edit.add(copyText);
        edit.add(pasteText);

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
