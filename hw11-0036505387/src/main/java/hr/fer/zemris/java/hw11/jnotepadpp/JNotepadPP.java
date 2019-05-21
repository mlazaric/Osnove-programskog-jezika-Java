package hr.fer.zemris.java.hw11.jnotepadpp;

import hr.fer.zemris.java.hw11.jnotepadpp.document.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.localization.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.KeyStore;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class JNotepadPP extends JFrame {

    private static final String TITLE = "JNotepad++";

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
    private Action showStats;
    private Action exitProgram;

    public JNotepadPP() {
        provider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

        setSize(500, 500);
        setTitle(TITLE);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exitProgram();
            }

        });

        initGUI();

        tabs.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                String title = TITLE;

                if (tabs.getCurrentDocument() != null) {
                    SingleDocumentModel current = tabs.getCurrentDocument();

                    if (current.getFilePath() == null) {
                        title = "(unnamed) - " + title;
                    }
                    else {
                        title = current.getFilePath().toAbsolutePath().toString() + " - " + title;
                    }
                }

                setTitle(title);
            }

        });
    }

    private void closeAllTabs() {
        for (SingleDocumentModel document : tabs) {
            discardOrSaveAs(document);
        }
    }

    private void initGUI() {
        tabs = new DefaultMultipleDocumentModel();

        add(tabs, BorderLayout.CENTER);

        initActions();
        createMenus();
        add(createToolbar(), BorderLayout.PAGE_START);
    }

    private void initActions() {
        createBlankDocument = createAction("create_blank_document", tabs::createNewDocument);
        openExistingDocument = createAction("open_existing_document", this::openExistingDocument);
        saveDocument = createAction("save_document", () -> saveDocument(tabs.getCurrentDocument()));
        saveAsDocument = createAction("save_as_document", () -> saveAsDocument(tabs.getCurrentDocument()));
        closeDocument = createAction("close_document", this::closeDocument);
        exitProgram = createAction("exit_program", this::exitProgram);

        cutText = createAction("cut_text", new DefaultEditorKit.CutAction());
        copyText = createAction("copy_text", new DefaultEditorKit.CopyAction());
        pasteText = createAction("paste_text", new DefaultEditorKit.PasteAction());

        showStats = createAction("stats_action", this::showStats);

        languageEn = createAction("language_en", () -> LocalizationProvider.getInstance().setLanguage("en"));
        languageHr = createAction("language_hr", () -> LocalizationProvider.getInstance().setLanguage("hr"));

        setKeys(createBlankDocument, KeyStroke.getKeyStroke("control N"), KeyEvent.VK_N);
        setKeys(openExistingDocument, KeyStroke.getKeyStroke("control O"), KeyEvent.VK_O);
        setKeys(saveDocument, KeyStroke.getKeyStroke("control S"), KeyEvent.VK_S);
        setKeys(saveAsDocument, KeyStroke.getKeyStroke("control shift S"), KeyEvent.VK_A);
        setKeys(closeDocument, KeyStroke.getKeyStroke("control W"), KeyEvent.VK_C);
        setKeys(exitProgram, KeyStroke.getKeyStroke("control alt X"), KeyEvent.VK_X);

        setKeys(cutText, KeyStroke.getKeyStroke("control X"), KeyEvent.VK_U);
        setKeys(copyText, KeyStroke.getKeyStroke("control C"), KeyEvent.VK_C);
        setKeys(pasteText, KeyStroke.getKeyStroke("control V"), KeyEvent.VK_P);

        setKeys(showStats, KeyStroke.getKeyStroke("control shift T"), KeyEvent.VK_T);

        setKeys(languageEn, KeyStroke.getKeyStroke("control alt E"), KeyEvent.VK_E);
        setKeys(languageHr, KeyStroke.getKeyStroke("control alt R"), KeyEvent.VK_R);

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
        showStats.setEnabled(false);
        conditionallyEnable(showStats, () -> tabs.getCurrentDocument() != null);

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
        if (keyStroke != null) {
            action.putValue(Action.ACCELERATOR_KEY, keyStroke);
        }

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
        file.addSeparator();
        file.add(exitProgram);

        JMenu edit = new LocalizableJMenu("edit", provider);

        menuBar.add(edit);

        edit.add(cutText);
        edit.add(copyText);
        edit.add(pasteText);

        JMenu statistics = new LocalizableJMenu("statistics", provider);

        menuBar.add(statistics);

        statistics.add(showStats);

        JMenu languages = new LocalizableJMenu("localization", provider);

        menuBar.add(languages);

        languages.add(languageEn);
        languages.add(languageHr);

        setJMenuBar(menuBar);
    }

    private JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar();

        toolBar.add(createBlankDocument);
        toolBar.add(openExistingDocument);
        toolBar.add(saveDocument);
        toolBar.add(saveAsDocument);
        toolBar.add(closeDocument);
        toolBar.add(exitProgram);
        toolBar.addSeparator();

        toolBar.add(cutText);
        toolBar.add(copyText);
        toolBar.add(pasteText);
        toolBar.addSeparator();

        toolBar.add(showStats);
        toolBar.addSeparator();

        toolBar.add(languageEn);
        toolBar.add(languageHr);

        return toolBar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JNotepadPP().setVisible(true);
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void discardOrSaveAs(SingleDocumentModel document) {
        if (document.isModified()) {
            String title = "<unnamed>";

            if (document.getFilePath() != null) {
                title = document.getFilePath().getFileName().toString();
            }

            int result = JOptionPane.showConfirmDialog(this, "Discard changes made to " + title + "?");

            if (result == JOptionPane.CANCEL_OPTION) {
                throw new RuntimeException("Cancelled");
            }
            else if (result == JOptionPane.NO_OPTION) {
                saveAsDocument(document);
            }
        }
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

    private void saveDocument(SingleDocumentModel model) {
        try {
            tabs.saveDocument(model, null);
        }
        catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void saveAsDocument(SingleDocumentModel model) {
        JFileChooser jfc = new JFileChooser();

        if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                tabs.saveDocument(model, jfc.getSelectedFile().toPath());
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
            discardOrSaveAs(document);
        }

        if (!document.isModified()) {
            tabs.closeDocument(document);
        }
    }

    private void showStats() {
        SingleDocumentModel current = tabs.getCurrentDocument();

        if (current == null) {
            return;
        }

        JTextArea text = current.getTextComponent();

        int characters = text.getText().length();
        int nonBlankCharacters = text.getText().replaceAll("\\s", "").length();
        int lines = text.getLineCount();

        String stats = String.format(provider.getString("stats_window"), characters, nonBlankCharacters, lines);

        JOptionPane.showMessageDialog(this, stats, "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitProgram() {
        try {
            closeAllTabs();

            dispose();
        }
        catch (Exception exc) {}
    }

}
