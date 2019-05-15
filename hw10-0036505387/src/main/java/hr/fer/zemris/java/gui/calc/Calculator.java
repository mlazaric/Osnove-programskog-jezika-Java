package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;

import static java.lang.Math.*;

/**
 * Creates a simple calculator GUI using a {@link CalcModel}.
 *
 * @author Marko Lazarić
 */
@SuppressWarnings("serial")
public class Calculator extends JFrame {

    /**
     * The background color of the display.
     */
    protected static final Color DISPLAY_BACKGROUND_COLOR = Color.YELLOW;

    /**
     * The border color of the display.
     */
    protected static final Color DISPLAY_BORDER_COLOR = Color.BLACK;

    /**
     * The background color of the buttons.
     */
    protected static final Color BUTTON_BACKGROUND_COLOR = Color.LIGHT_GRAY;

    /**
     * The model to use for calculation.
     */
    private final CalcModel model;

    /**
     * The stack used for stack operation buttons.
     */
    private final Stack<Double> stack;

    /**
     * Creates a new {@link Calculator} with the given argument.
     *
     * @param model the model used for calculation
     */
    public Calculator(CalcModel model) {
        this.model = model;
        this.stack = new Stack<>();

        initGUI();
        pack();
    }

    /**
     * Initialises the GUI.
     */
    private void initGUI() {
        Container container = getContentPane();

        container.setLayout(new CalcLayout(3));

        addDisplay(container);
        addDigits(container);
        addSignAndPoint(container);
        addBinaryOperators(container);
        addCalculateButton(container);
        addClearAndReset(container);
        addStackButtons(container);
        addInvertibleOperations(container);
    }

    /**
     * Adds all the invertible buttons.
     *
     * @param container the container to add them to
     */
    private void addInvertibleOperations(Container container) {
        JCheckBox isInvertedCheck = new JCheckBox("Inv");

        container.add(isInvertedCheck, new RCPosition(5, 7));

        InvertibleButton[] invertibleButtons = {
                new InvertibleButton(
                        "1/x",
                        () -> model.setValue(1 / model.getValue()),
                        "1/x",
                        () -> model.setValue(1 / model.getValue())),

                new InvertibleButton(
                        "log",
                        () -> model.setValue(log10(model.getValue())),
                        "10^x",
                        () -> model.setValue(pow(10, model.getValue()))),

                new InvertibleButton(
                        "ln",
                        () -> model.setValue(log(model.getValue())),
                        "e^x",
                        () -> model.setValue(exp(model.getValue()))),

                new InvertibleButton(
                        "x^n",
                        () -> performOperation((a, b) -> pow(a, b)),
                        "x^(1/n)",
                        () -> performOperation((a, b) -> pow(a, 1.0 / b))),

                new InvertibleButton(
                        "sin",
                        () -> model.setValue(sin(model.getValue())),
                        "arcsin",
                        () -> model.setValue(asin(model.getValue()))),

                new InvertibleButton(
                        "cos",
                        () -> model.setValue(cos(model.getValue())),
                        "arccos",
                        () -> model.setValue(acos(model.getValue()))),

                new InvertibleButton(
                        "tan",
                        () -> model.setValue(tan(model.getValue())),
                        "arctan",
                        () -> model.setValue(atan(model.getValue()))),

                new InvertibleButton(
                        "ctg",
                        () -> model.setValue(1 / tan(model.getValue())),
                        "arcctg",
                        () -> model.setValue(atan(1 / model.getValue()))),
        };

        for (int index = 0; index < invertibleButtons.length; ++index) {
            RCPosition position = new RCPosition(2 + index % 4, 1 + index / 4);

            container.add(invertibleButtons[index], position);
            isInvertedCheck.addItemListener(invertibleButtons[index]);
        }
    }

    /**
     * Adds the buttons related to stack operations to the container.
     *
     * @param container the container to add them to
     */
    private void addStackButtons(Container container) {
        JButton pushButton = new JButton("push");
        JButton popButton = new JButton("pop");

        pushButton.addActionListener(e -> {
            stack.push(model.getValue());
            model.clear();
        });
        popButton.addActionListener(e -> {
            if (stack.isEmpty()) {
                showError("Stack is empty.");
                return;
            }

            model.setValue(stack.pop());
        });

        pushButton.setBackground(BUTTON_BACKGROUND_COLOR);
        popButton.setBackground(BUTTON_BACKGROUND_COLOR);

        container.add(pushButton, new RCPosition(3, 7));
        container.add(popButton, new RCPosition(4, 7));
    }

    /**
     * Adds clear and reset buttons to the container.
     *
     * @param container the container to add them to
     */
    private void addClearAndReset(Container container) {
        JButton clearButton = new JButton("clr");
        JButton resetButton = new JButton("res");

        clearButton.addActionListener(e -> model.clear());
        resetButton.addActionListener(e -> model.clearAll());

        clearButton.setBackground(BUTTON_BACKGROUND_COLOR);
        resetButton.setBackground(BUTTON_BACKGROUND_COLOR);

        container.add(clearButton, new RCPosition(1, 7));
        container.add(resetButton, new RCPosition(2, 7));
    }

    /**
     * Adds the calculate button ('=') to the container.
     *
     * @param container the container to add it to
     */
    private void addCalculateButton(Container container) {
        JButton calculateButton = new JButton("=");
        RCPosition position = new RCPosition(1, 6);

        container.add(calculateButton, position);

        calculateButton.setBackground(BUTTON_BACKGROUND_COLOR);

        calculateButton.addActionListener(e -> {
            double value = model.getValue();

            if (model.isActiveOperandSet()) {
                value = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
            }

            model.clearAll();
            model.setValue(value);
        });
    }

    /**
     * Adds the binary operators to the container.
     *
     * @param container the container to add them to
     */
    private void addBinaryOperators(Container container) {
        String[] operators = {"/", "*", "-", "+"};
        DoubleBinaryOperator[] functions = {
                (a, b) -> a / b,
                (a, b) -> a * b,
                (a, b) -> a - b,
                (a, b) -> a + b
        };


        for (int index = 0; index < operators.length; index++) {
            JButton operatorButton = new JButton(operators[index]);
            RCPosition position = new RCPosition(2 + index, 6);

            container.add(operatorButton, position);

            operatorButton.setBackground(BUTTON_BACKGROUND_COLOR);

            DoubleBinaryOperator operation = functions[index];

            operatorButton.addActionListener(e -> {
                performOperation(operation);
            });
        }
    }

    /**
     * Perform a binary operation and set the new operation.
     *
     * @param operation the new operation to set
     */
    private void performOperation(DoubleBinaryOperator operation) {
        if (model.isActiveOperandSet()) {
            model.setActiveOperand(model.getPendingBinaryOperation()
                                        .applyAsDouble(model.getActiveOperand(), model.getValue()));
        }
        else {
            model.setActiveOperand(model.getValue());
        }

        model.setValue(model.getActiveOperand());
        model.setPendingBinaryOperation(operation);
    }

    /**
     * Adds the buttons for swapping sign and adding decimal point.
     *
     * @param container the container to add them to
     */
    private void addSignAndPoint(Container container) {
        JButton signButton = new JButton("+/-");
        JButton pointButton = new JButton(".");

        container.add(signButton, new RCPosition(5, 4));
        container.add(pointButton, new RCPosition(5, 5));

        signButton.addActionListener(e -> {
            if (!model.isEditable()) {
                model.clear();
            }

            model.swapSign();
        });
        signButton.setBackground(BUTTON_BACKGROUND_COLOR);

        pointButton.addActionListener(e -> {
            if (!model.isEditable()) {
                model.clear();
            }

            try {
                model.insertDecimalPoint();
            }
            catch (CalculatorInputException exp) {
                showError(exp.getMessage());
            }
        });
        pointButton.setBackground(BUTTON_BACKGROUND_COLOR);
    }

    /**
     * Adds the display to the container.
     *
     * @param container the container to add it to
     */
    private void addDisplay(Container container) {
        JLabel display = new JLabel(model.toString());

        model.addCalcValueListener(m -> display.setText(model.toString()));

        display.setFont(display.getFont().deriveFont(30f));

        JPanel displayPanel = new JPanel();

        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(DISPLAY_BACKGROUND_COLOR);
        displayPanel.setBorder(BorderFactory.createLineBorder(DISPLAY_BORDER_COLOR));

        displayPanel.add(display, BorderLayout.LINE_END);
        container.add(displayPanel, new RCPosition(1, 1));
    }

    /**
     * Adds all the digit buttons to the container.
     *
     * @param container the container to add it to
     */
    private void addDigits(Container container) {
        for (int digit = 0; digit < 10; ++digit) {
            // Calculate the position of the digit
            int rowOfDigit = 4 - ((digit - 1) / 3);
            int colOfDigit = 3 + ((digit - 1) % 3);

            // Zero has a specific position
            if (digit == 0) {
                rowOfDigit = 5;
                colOfDigit = 3;
            }

            int currentDigit = digit;

            JButton digitButton = new JButton("" + digit);

            container.add(digitButton, new RCPosition(rowOfDigit, colOfDigit));

            digitButton.addActionListener(e -> {
                if (!model.isEditable()) {
                    model.clear();
                }

                model.insertDigit(currentDigit);
            });
            digitButton.setBackground(BUTTON_BACKGROUND_COLOR);
            digitButton.setFont(digitButton.getFont().deriveFont(30f));
        }
    }

    /**
     * Shows an error dialog with the specified message.
     *
     * @param message the error message
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Opens a calculator GUI.
     *
     * @param args the arguments are ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new Calculator(new CalcModelImpl()).setVisible(true);
        });
    }

}
