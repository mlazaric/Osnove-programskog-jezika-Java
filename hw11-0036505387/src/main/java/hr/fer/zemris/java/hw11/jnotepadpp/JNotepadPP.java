package hr.fer.zemris.java.hw11.jnotepadpp;

import hr.fer.zemris.java.hw11.jnotepadpp.document.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.localization.*;

import javax.print.Doc;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;

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
    private Action languageDe;
    private Action toUpperCase;
    private Action toLowerCase;
    private Action invertCase;
    private Action sortAsc;
    private Action sortDesc;
    private Action unique;

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

    private JLabel createClock() {
        JLabel clock = new JLabel("", JLabel.RIGHT);

        new Timer(true).scheduleAtFixedRate(new TimerTask() {

            private final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            @Override
            public void run() {
                String text = format.format(Calendar.getInstance().getTime());

                clock.setText(text);
            }

        }, 1000, 1000);

        return clock;
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

        JTextComponent text = current.getTextComponent();

        int characters = text.getText().length();
        int nonBlankCharacters = text.getText().replaceAll("\\s", "").length();
        int lines = text.getDocument().getDefaultRootElement().getElementCount();

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

    private void toUpperCase() {
        transformSelection(true, false);
    }

    private void toLowerCase() {
        transformSelection(false, true);
    }

    private void invertCase() {
        transformSelection(true, true);
    }

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

    private void sortDescending() {
        transformSelectedLines(lines -> {
            Locale locale = new Locale(provider.getCurrentLanguage());
            Collator collator = Collator.getInstance(locale);

            Arrays.sort(lines, collator.reversed());

            return lines;
        });
    }

    private void sortAscending() {
        transformSelectedLines(lines -> {
            Locale locale = new Locale(provider.getCurrentLanguage());
            Collator collator = Collator.getInstance(locale);

            Arrays.sort(lines, collator);

            return lines;
        });
    }

    private void unique() {
        transformSelectedLines(lines -> {
            return Stream.of(lines)
                         .distinct()
                         .collect(Collectors.toList())
                         .toArray(new String[0]);
        });
    }

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
