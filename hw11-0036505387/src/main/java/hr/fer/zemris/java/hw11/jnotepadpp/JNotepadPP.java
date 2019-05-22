package hr.fer.zemris.java.hw11.jnotepadpp;

import hr.fer.zemris.java.hw11.jnotepadpp.document.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * A simple localizable notepad program with support for multiple tabs.
 *
 * @author Marko Lazarić
 */
@SuppressWarnings("serial")
public class JNotepadPP extends JFrame {

    /**
     * The title fo the program.
     */
    private static final String TITLE = "JNotepad++";

    /**
     * The currently opened documents.
     */
    private DefaultMultipleDocumentModel tabs;

    /**
     * The localization provider tied to this {@link JFrame}.
     */
    private FormLocalizationProvider provider;

    /**
     * Creates a new blank document.
     */
    private Action createBlankDocument;

    /**
     * Opens existing document.
     */
    private Action openExistingDocument;

    /**
     * Saves document to the current file path.
     */
    private Action saveDocument;

    /**
     * Saves document to a new filep ath.
     */
    private Action saveAsDocument;

    /**
     * Closes current document.
     */
    private Action closeDocument;

    /**
     * Exits program.
     */
    private Action exitProgram;

    /**
     * Switches the current language to English.
     */
    private Action languageEn;

    /**
     * Switches the current language to Croatian.
     */
    private Action languageHr;

    /**
     * Switches the current language to German.
     */
    private Action languageDe;

    /**
     * Cuts the currently selected text.
     */
    private Action cutText;

    /**
     * Copies the currently selected text.
     */
    private Action copyText;

    /**
     * Pastes the contents of the clipboard.
     */
    private Action pasteText;

    /**
     * Shows some information about the current document.
     */
    private Action showStats;

    /**
     * Changes case of all selected characters to upper case.
     */
    private Action toUpperCase;

    /**
     * Changes case of all selected characters to lower case.
     */
    private Action toLowerCase;

    /**
     * Inverts case of all selected characters.
     */
    private Action invertCase;

    /**
     * Sorts the selected lines in ascending order.
     */
    private Action sortAsc;

    /**
     * Sorts the selected lines in descending order.
     */
    private Action sortDesc;

    /**
     * Removes duplicate lines from selected lines.
     */
    private Action unique;

    /**
     * Creates new {@link JNotepadPP}.
     */
    public JNotepadPP() {
        provider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

        setSize(750, 750);
        setTitle(TITLE);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exitProgram();
            }

        });

        initGUI();

        tabs.addMultipleDocumentListener(new MultipleDocumentListener() {
            private final SingleDocumentListener listener = new SingleDocumentListener() {
                @Override
                public void documentModifyStatusUpdated(SingleDocumentModel model) {}

                @Override
                public void documentFilePathUpdated(SingleDocumentModel model) {
                    updateTitle(model);
                }
            };

            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if (previousModel != null) {
                    previousModel.removeSingleDocumentListener(listener);
                }
                if (currentModel != null) {
                    currentModel.addSingleDocumentListener(listener);
                }


                updateTitle(currentModel);
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {}

            @Override
            public void documentRemoved(SingleDocumentModel model) {}
        });
    }

    /**
     * Updates the title of the application.
     */
    private void updateTitle(SingleDocumentModel currentModel) {
        String title = TITLE;

        if (currentModel != null) {

            if (currentModel.getFilePath() == null) {
                title = "(unnamed) - " + title;
            }
            else {
                title = currentModel.getFilePath().toAbsolutePath().toString() + " - " + title;
            }
        }

        setTitle(title);
    }

    /**
     * Initialises the GUI.
     */
    private void initGUI() {
        JPanel panel = new JPanel(new BorderLayout());

        tabs = new DefaultMultipleDocumentModel();
        var statusBar = createStatusBar();

        panel.add(tabs, BorderLayout.CENTER);
        panel.add(statusBar, BorderLayout.PAGE_END);

        add(panel, BorderLayout.CENTER);

        initActions();
        createMenus();
        add(createToolbar(), BorderLayout.PAGE_START);
    }

    /**
     * Initialises the actions, their shortcuts and whether they are enabled.
     */
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

        toUpperCase = createAction("to_upper_case", this::toUpperCase);
        toLowerCase = createAction("to_lower_case", this::toLowerCase);
        invertCase = createAction("invert_case", this::invertCase);

        sortAsc = createAction("sort_asc", this::sortAscending);
        sortDesc = createAction("sort_desc", this::sortDescending);

        unique = createAction("unique", this::unique);

        languageEn = createAction("language_en", () -> LocalizationProvider.getInstance().setLanguage("en"));
        languageHr = createAction("language_hr", () -> LocalizationProvider.getInstance().setLanguage("hr"));
        languageDe = createAction("language_de", () -> LocalizationProvider.getInstance().setLanguage("de"));

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

        setKeys(toUpperCase, KeyStroke.getKeyStroke("control shift alt U"), KeyEvent.VK_U);
        setKeys(toLowerCase, KeyStroke.getKeyStroke("control shift alt L"), KeyEvent.VK_L);
        setKeys(invertCase, KeyStroke.getKeyStroke("control shift alt I"), KeyEvent.VK_I);

        setKeys(sortAsc, KeyStroke.getKeyStroke("control shift alt A"), KeyEvent.VK_A);
        setKeys(sortAsc, KeyStroke.getKeyStroke("control shift alt D"), KeyEvent.VK_D);

        setKeys(unique, KeyStroke.getKeyStroke("control shift alt Q"), KeyEvent.VK_Q);

        setKeys(languageEn, KeyStroke.getKeyStroke("control alt E"), KeyEvent.VK_E);
        setKeys(languageHr, KeyStroke.getKeyStroke("control alt R"), KeyEvent.VK_R);
        setKeys(languageDe, KeyStroke.getKeyStroke("control alt D"), KeyEvent.VK_D);

        // Should only be enabled if the current document is modified and has path set
        saveDocument.setEnabled(false);
        conditionallyEnableBasedOnCurrentDocument(saveDocument, model -> model != null &&
                                                   model.isModified() &&
                                                   model.getFilePath() != null);

        // Should only be enabled if the current document is modified
        saveAsDocument.setEnabled(false);
        conditionallyEnableBasedOnCurrentDocument(saveAsDocument, model -> model != null && model.isModified());

        // Should only be enabled if there is a documents open
        Supplier<Boolean> ENABLED_IF_A_DOCUMENT_IS_OPEN = () -> tabs.getCurrentDocument() != null;

        closeDocument.setEnabled(false);
        conditionallyEnableBasedOnNumberOfDocuments(closeDocument, ENABLED_IF_A_DOCUMENT_IS_OPEN);

        // Should only be enabled if there is a document open
        showStats.setEnabled(false);
        conditionallyEnableBasedOnNumberOfDocuments(showStats, ENABLED_IF_A_DOCUMENT_IS_OPEN);

        // Should only be enabled if there is a document open, notice you can copy and cut an empty selection
        cutText.setEnabled(false);
        copyText.setEnabled(false);
        pasteText.setEnabled(false);
        conditionallyEnableBasedOnNumberOfDocuments(cutText, ENABLED_IF_A_DOCUMENT_IS_OPEN);
        conditionallyEnableBasedOnNumberOfDocuments(copyText, ENABLED_IF_A_DOCUMENT_IS_OPEN);
        conditionallyEnableBasedOnNumberOfDocuments(pasteText, ENABLED_IF_A_DOCUMENT_IS_OPEN);

        // Should only be enabled if there is something selected
        Predicate<SingleDocumentModel> ENABLED_IF_SOMETHING_IS_SELECTED = current ->
                current != null &&
                current.getTextComponent().getSelectedText() != null;

        toUpperCase.setEnabled(false);
        toLowerCase.setEnabled(false);
        invertCase.setEnabled(false);

        conditionallyEnableBasedOnSelection(toUpperCase, ENABLED_IF_SOMETHING_IS_SELECTED);
        conditionallyEnableBasedOnSelection(toLowerCase, ENABLED_IF_SOMETHING_IS_SELECTED);
        conditionallyEnableBasedOnSelection(invertCase, ENABLED_IF_SOMETHING_IS_SELECTED);

        // Should only be enabled if there is something selected
        sortAsc.setEnabled(false);
        sortDesc.setEnabled(false);
        unique.setEnabled(false);

        conditionallyEnableBasedOnSelection(sortAsc, ENABLED_IF_SOMETHING_IS_SELECTED);
        conditionallyEnableBasedOnSelection(sortDesc, ENABLED_IF_SOMETHING_IS_SELECTED);
        conditionallyEnableBasedOnSelection(unique, ENABLED_IF_SOMETHING_IS_SELECTED);
    }

    /**
     * Conditionally enables action based on the selection.
     * The status of the action is checked after every {@link CaretEvent} or when the current document changes.
     *
     * @param action the action to conditionally enable
     * @param shouldEnable the predicate used to determine whether it should be enabled or disabled
     */
    private void conditionallyEnableBasedOnSelection(Action action, Predicate<SingleDocumentModel> shouldEnable) {
        tabs.addMultipleDocumentListener(new MultipleDocumentListener() {
            private final CaretListener listener = e -> action.setEnabled(shouldEnable.test(tabs.getCurrentDocument()));

            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if (previousModel != null) {
                    previousModel.getTextComponent().removeCaretListener(listener);
                }

                if (currentModel != null) {
                    currentModel.getTextComponent().addCaretListener(listener);
                }

                action.setEnabled(shouldEnable.test(currentModel));
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {}

            @Override
            public void documentRemoved(SingleDocumentModel model) {}
        });
    }

    /**
     * Conditionally enables action based on the current document.
     * The status of the action is checked when the current document changes, its modify status is changed or its
     * file path is changed.
     *
     * @param action the action to conditionally enable
     * @param shouldEnable the predicate used to determine whether it should be enabled or disabled
     */
    private void conditionallyEnableBasedOnCurrentDocument(Action action, Predicate<SingleDocumentModel> shouldEnable) {
        tabs.addMultipleDocumentListener(new MultipleDocumentListener() {
            private final SingleDocumentListener listener = new SingleDocumentListener() {

                @Override
                public void documentModifyStatusUpdated(SingleDocumentModel model) {
                    action.setEnabled(shouldEnable.test(model));
                }

                @Override
                public void documentFilePathUpdated(SingleDocumentModel model) {
                    action.setEnabled(shouldEnable.test(model));
                }

            };

            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if (previousModel != null) {
                    previousModel.removeSingleDocumentListener(listener);
                }

                if (currentModel != null) {
                    currentModel.addSingleDocumentListener(listener);
                }

                action.setEnabled(shouldEnable.test(currentModel));
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {}

            @Override
            public void documentRemoved(SingleDocumentModel model) {}
        });
    }

    /**
     * Conditionally enables action based on the number of documents that are currently open.
     * The status of the action is checked when a document is added or removed.
     *
     * @param action the action to conditionally enable
     * @param shouldEnable the supplier used to determine whether it should be enabled or disabled
     */
    private void conditionallyEnableBasedOnNumberOfDocuments(Action action, Supplier<Boolean> shouldEnable) {
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

    /**
     * Sets the key shortcut and the mnemonic for the specified action.
     *
     * @param action the action to set the shortcut and the mnemonic for
     * @param keyStroke the keyboard shortcut
     * @param keyEvent the mnemonic
     */
    private void setKeys(Action action, KeyStroke keyStroke, int keyEvent) {
        if (keyStroke != null) {
            action.putValue(Action.ACCELERATOR_KEY, keyStroke);
        }

        action.putValue(Action.MNEMONIC_KEY, keyEvent);
    }

    /**
     * Creates a {@link LocalizableAction} with the specified key and using the specified {@link Runnable}.
     *
     * @param key the key used for localization
     * @param runnable the task to run when the action is performed
     * @return the newly created {@link LocalizableAction}
     */
    @SuppressWarnings("serial")
    private Action createAction(String key, Runnable runnable) {

        return new LocalizableAction(key, provider) {

            @Override
            public void actionPerformed(ActionEvent e) {
                runnable.run();
            }

        };
    }

    /**
     * Creates a {@link LocalizableAction} with the specified key by masking a current action.
     *
     * @param key the key used for localization
     * @param action the action to mask
     * @return the newly created {@link LocalizableAction}
     */
    private Action createAction(String key, Action action) {
        return createAction(key, () -> action.actionPerformed(null));
    }

    /**
     * Creates the status bar.
     *
     * @return the status bar
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());

        // Add some margins so it does not look as cramped
        //   https://stackoverflow.com/a/5328475/11172828
        statusBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, Color.LIGHT_GRAY),
                                                               BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        statusBar.add(createStats(), BorderLayout.LINE_START);
        statusBar.add(createClock(), BorderLayout.CENTER);

        return statusBar;
    }

    /**
     * Creates the statistics part of the status bar.
     *
     * @return the statistics panel
     */
    private JPanel createStats() {
        JLabel caretLabel = new JLabel("", JLabel.LEFT);
        JLabel documentLabel = new JLabel("");

        CaretListener caretListener = e -> updateCaretLabel(caretLabel);
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateDocumentLabel(documentLabel);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateDocumentLabel(documentLabel);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateDocumentLabel(documentLabel);
            }
        };

        tabs.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if (previousModel != null) {
                    previousModel.getTextComponent().removeCaretListener(caretListener);
                    previousModel.getTextComponent().getDocument().removeDocumentListener(documentListener);
                }

                if (currentModel != null) {
                    currentModel.getTextComponent().addCaretListener(caretListener);
                    currentModel.getTextComponent().getDocument().addDocumentListener(documentListener);
                }

                updateCaretLabel(caretLabel);
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {}

            @Override
            public void documentRemoved(SingleDocumentModel model) {}
        });

        JPanel panel = new JPanel(new GridLayout(1, 2));

        panel.add(documentLabel);
        documentLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.LIGHT_GRAY));
        panel.add(caretLabel);
        caretLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));

        return panel;
    }

    /**
     * Updates the part of the status bar which depends only on the currently open document.
     *
     * @param documentLabel the label that depends only on the currently open document
     */
    private void updateDocumentLabel(JLabel documentLabel) {
        SingleDocumentModel currentDocument = tabs.getCurrentDocument();

        if (currentDocument == null) {
            documentLabel.setText("");
            return;
        }

        JTextComponent text = currentDocument.getTextComponent();

        int length = text.getText().length();

        String labelText = String.format(provider.getString("document_label"), length);

        documentLabel.setText(labelText);
    }

    /**
     * Updates the part of the status bar which depends on the caret position.
     *
     * @param caretLabel the label that depends on the caret position
     */
    private void updateCaretLabel(JLabel caretLabel) {
        SingleDocumentModel currentDocument = tabs.getCurrentDocument();

        if (currentDocument == null) {
            caretLabel.setText("");
            return;
        }

        JTextComponent text = currentDocument.getTextComponent();
        Document doc = text.getDocument();
        Element root = doc.getDefaultRootElement();

        int line = root.getElementIndex(text.getCaretPosition()) + 1;
        int column = text.getCaretPosition() - root.getElement(line - 1).getStartOffset() + 1;
        int selection = text.getSelectedText() == null ? 0 : text.getSelectedText().length();

        String labelText = String.format(provider.getString("caret_label"), line, column, selection);

        caretLabel.setText(labelText);
    }

    /**
     * Creates a right aligned clock label.
     *
     * @return the clock label
     */
    private JLabel createClock() {
        JLabel clock = new ClockLabel(this);

        clock.setHorizontalAlignment(JLabel.RIGHT);

        return clock;
    }

    /**
     * Creates menus from the actions.
     */
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

        JMenu tools = new LocalizableJMenu("tools", provider);

        menuBar.add(tools);

        JMenu changeCase = new LocalizableJMenu("change_case", provider);

        tools.add(changeCase);

        changeCase.add(toUpperCase);
        changeCase.add(toLowerCase);
        changeCase.add(invertCase);

        JMenu sort = new LocalizableJMenu("sort", provider);

        tools.add(sort);

        sort.add(sortAsc);
        sort.add(sortDesc);

        tools.add(unique);

        JMenu languages = new LocalizableJMenu("languages", provider);

        menuBar.add(languages);

        languages.add(languageEn);
        languages.add(languageHr);
        languages.add(languageDe);

        setJMenuBar(menuBar);
    }

    /**
     * Creates the toolbar using the actions and returns it.
     *
     * @return the toolbar
     */
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

        toolBar.add(toUpperCase);
        toolBar.add(toLowerCase);
        toolBar.add(invertCase);
        toolBar.addSeparator();

        toolBar.add(sortAsc);
        toolBar.add(sortDesc);
        toolBar.addSeparator();

        toolBar.add(unique);
        toolBar.addSeparator();

        toolBar.add(languageEn);
        toolBar.add(languageHr);
        toolBar.add(languageDe);

        return toolBar;
    }

    /**
     * Opens {@link JNotepadPP}.
     *
     * @param args the arguments are ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JNotepadPP().setVisible(true);
        });
    }

    /**
     * Shows error message in a {@link JOptionPane}.
     *
     * @param message the message to show
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, provider.getString("error"), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Checks modified status of each tab and prompts the user to save or discard it.
     */
    private void closeAllTabs() {
        for (SingleDocumentModel document : tabs) {
            discardOrSaveAs(document, false);
        }
    }

    /**
     * Prompts the user to save or discard the document.
     *
     * @param document the document to save or discard
     * @param shouldClose whether it should close the tab if the user discards it
     */
    private void discardOrSaveAs(SingleDocumentModel document, boolean shouldClose) {
        if (document.isModified()) {
            String title = "<unnamed>";

            if (document.getFilePath() != null) {
                title = document.getFilePath().getFileName().toString();
            }

            String message = String.format(provider.getString("discard_changes"), title);
            int result = JOptionPane.showConfirmDialog(this, message);

            if (result == JOptionPane.CANCEL_OPTION) {
                throw new RuntimeException("Cancelled");
            }
            else if (result == JOptionPane.NO_OPTION) {
                if (!saveAsDocument(document)) {
                    throw new RuntimeException("Cancelled");
                }
                else if (shouldClose) {
                    tabs.closeDocument(document);
                }
            }
            else if (result == JOptionPane.YES_OPTION && shouldClose) {
                tabs.closeDocument(document);
            }
        }
    }

    /**
     * Opens an existing file from the disk.
     */
    private void openExistingDocument() {
        JFileChooser jfc = new JFileChooser();

        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (!Files.isReadable(jfc.getSelectedFile().toPath())) {
                showError(provider.getString("not_readable"));
            }

            try {
                tabs.loadDocument(jfc.getSelectedFile().toPath());
            }
            catch (RuntimeException e) {
                showError(e.getMessage());
            }
        }
    }

    /**
     * Saves the document to disk using its file path.
     *
     * @param model the document to save
     */
    private void saveDocument(SingleDocumentModel model) {
        try {
            tabs.saveDocument(model, null);
        }
        catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    /**
     * Prompts user for a path and saves the file to that location.
     *
     * @param model the document to save
     *
     * @return true if it has been saved, false otherwise
     */
    private boolean saveAsDocument(SingleDocumentModel model) {
        JFileChooser jfc = new JFileChooser();

        if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (Files.exists(jfc.getSelectedFile().toPath())) {
                int result = JOptionPane.showConfirmDialog(this, provider.getString("overwrite_file"),
                        provider.getString("question"), JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.NO_OPTION) {
                    return false;
                }
            }

            try {
                tabs.saveDocument(model, jfc.getSelectedFile().toPath());
            }
            catch (RuntimeException e) {
                showError(e.getMessage());
            }

            return true;
        }

        return false;
    }

    /**
     * Closes the currently open document.
     * Prompts the user to save or discard if it has been modified.
     */
    private void closeDocument() {
        SingleDocumentModel document = tabs.getCurrentDocument();

        if (document == null) {
            return;
        }

        if (document.isModified()) {
            try {
                discardOrSaveAs(document, true);
            }
            catch (RuntimeException cancelled) {}
        }
        else {
            tabs.closeDocument(document);
        }
    }

    /**
     * Shows the statistics about this document.
     */
    private void showStats() {
        SingleDocumentModel current = tabs.getCurrentDocument();

        if (current == null) {
            return;
        }

        JTextComponent text = current.getTextComponent();

        int characters = text.getText().length();
        int nonBlankCharacters = text.getText().replaceAll("\\s", "").length();
        int lines = text.getDocument().getDefaultRootElement().getElementCount();

        String stats = String.format(provider.getString("stats_window"), characters, nonBlankCharacters, lines);

        JOptionPane.showMessageDialog(this, stats, provider.getString("statistics"), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Exits the program after discarding or saving all tabs.
     */
    private void exitProgram() {
        try {
            closeAllTabs();

            dispose();
        }
        catch (Exception exc) {}
    }

    /**
     * Changes all selected characters to upper case.
     */
    private void toUpperCase() {
        transformSelection(true, false);
    }

    /**
     * Changes all selected characters to lower case.
     */
    private void toLowerCase() {
        transformSelection(false, true);
    }

    /**
     * Inverts the case of all selected characters.
     */
    private void invertCase() {
        transformSelection(true, true);
    }

    /**
     * Transforms the case of the selected characters.
     *
     * @param invertLowerCase should it invert the case of lower cased characters
     * @param invertUpperCase should it invert the case of upper cased characters
     */
    private void transformSelection(boolean invertLowerCase, boolean invertUpperCase) {
        if (tabs.getCurrentDocument() == null) {
            return;
        }

        JTextComponent textComponent = tabs.getCurrentDocument().getTextComponent();

        if (textComponent.getSelectedText() == null) {
            return;
        }

        char[] selection = textComponent.getSelectedText().toCharArray();
        StringBuilder sb = new StringBuilder();

        for (char c : selection) {
            if (Character.isUpperCase(c) && invertUpperCase) {
                sb.append(Character.toLowerCase(c));
            }
            else if (Character.isLowerCase(c) && invertLowerCase) {
                sb.append(Character.toUpperCase(c));
            }
            else {
                sb.append(c);
            }
        }

        textComponent.replaceSelection(sb.toString());
    }

    /**
     * Sorts the selected lines in descending order.
     */
    private void sortDescending() {
        transformSelectedLines(lines -> {
            Locale locale = new Locale(provider.getCurrentLanguage());
            Collator collator = Collator.getInstance(locale);

            Arrays.sort(lines, collator.reversed());

            return lines;
        });
    }

    /**
     * Sorts the selected lines in ascending order.
     */
    private void sortAscending() {
        transformSelectedLines(lines -> {
            Locale locale = new Locale(provider.getCurrentLanguage());
            Collator collator = Collator.getInstance(locale);

            Arrays.sort(lines, collator);

            return lines;
        });
    }

    /**
     * Removes duplicate lines from selected lines.
     */
    private void unique() {
        transformSelectedLines(lines -> {
            return Stream.of(lines)
                         .distinct()
                         .toArray(String[]::new);
        });
    }

    /**
     * Transforms selected lines and replaces them with the transformed lines.
     *
     * @param transformation the function used to transform lines to different lines
     */
    private void transformSelectedLines(Function<String[], String[]> transformation) {
        if (tabs.getCurrentDocument() == null) {
            return;
        }

        JTextComponent textComponent = tabs.getCurrentDocument().getTextComponent();

        Document doc = textComponent.getDocument();
        Caret caret = textComponent.getCaret();
        Element root = doc.getDefaultRootElement();

        int mark = caret.getMark();
        int dot = caret.getDot();

        int selectionStart = min(mark, dot);
        int selectionEnd = max(mark, dot);

        int firstLine = root.getElementIndex(selectionStart);
        int lastLine = root.getElementIndex(selectionEnd);

        int startOfFirstLine = root.getElement(firstLine).getStartOffset();
        int endOfLastLine = min(root.getElement(lastLine).getEndOffset(), doc.getLength());

        try {
            String text = doc.getText(startOfFirstLine, endOfLastLine - startOfFirstLine);
            String[] lines = text.split("\n");

            String transformed = String.join("\n", transformation.apply(lines)) + "\n";

            doc.remove(startOfFirstLine, endOfLastLine - startOfFirstLine);
            doc.insertString(startOfFirstLine, transformed, null);
        } catch (BadLocationException ignored) {}
    }

}
