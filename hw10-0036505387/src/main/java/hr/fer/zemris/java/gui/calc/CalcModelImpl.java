package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

import static java.lang.Math.abs;

/**
 * Implementation of {@link CalcModel} for {@link Calculator}.
 *
 * @author Marko Lazarić
 */
public class CalcModelImpl implements CalcModel {

    /**
     * Is the value currently editable?
     */
    private boolean isEditable = true;

    /**
     * Is the value positive?
     */
    private boolean isPositive = true;

    /**
     * The current string input.
     */
    private String currentInput = "";

    /**
     * The current value.
     */
    private double value = 0;

    /**
     * The active operand to use in the pending operation.
     */
    private double activeOperand;

    /**
     * The pending operation to perform.
     */
    private DoubleBinaryOperator pendingOperation;

    /**
     * Is the active operand currently set?
     */
    private boolean isActiveOperandSet = false;

    /**
     * The listeners that should be notified on value change.
     */
    private List<CalcValueListener> listeners = new ArrayList<>();

    /**
     * Notifies all listeners.
     */
    private void notifyListeners() {
        for (CalcValueListener listener : listeners) {
            listener.valueChanged(this);
        }
    }

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        Objects.requireNonNull(l, "Cannot add null listener.");

        // Copy on write to prevent any concurrent modification exceptions.
        listeners = new ArrayList<>(listeners);

        listeners.add(l);
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        Objects.requireNonNull(l, "Cannot remove null listener.");

        // Copy on write to prevent any concurrent modification exceptions.
        listeners = new ArrayList<>(listeners);

        listeners.remove(l);
    }

    @Override
    public double getValue() {
        return value  * (isPositive ? 1 : -1);
    }

    @Override
    public void setValue(double value) {
        this.value = abs(value);
        this.currentInput = Double.toString(this.value);
        this.isEditable = false;
        this.isPositive = value >= 0;

        notifyListeners();
    }

    @Override
    public boolean isEditable() {
        return isEditable;
    }

    @Override
    public void clear() {
        isEditable = true;
        isPositive = true;
        currentInput = "";
        value = 0;

        notifyListeners();
    }

    @Override
    public void clearAll() {
        clear();
        clearActiveOperand();

        pendingOperation = null;
    }

    @Override
    public void swapSign() throws CalculatorInputException {
        if (!isEditable) {
            throw new CalculatorInputException("Model is not editable.");
        }

        isPositive = !isPositive;
        notifyListeners();
    }

    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if (!isEditable) {
            throw new CalculatorInputException("Model is not editable.");
        }

        if (currentInput.indexOf('.') != -1) {
            throw new CalculatorInputException("Already contains one decimal point.");
        }

        if (currentInput.isEmpty()) {
            throw new CalculatorInputException("Cannot add decimal point to empty string.");
        }

        currentInput += ".";
    }

    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if (!isEditable) {
            throw new CalculatorInputException("Model is not editable.");
        }

        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("'" + digit + "' is not a valid digit");
        }

        String newInput = removeLeadingZeros(currentInput + digit);

        try {
            double newValue = Double.parseDouble(newInput);

            if (Double.isInfinite(newValue)) {
                throw new CalculatorInputException("'" + newInput + "' is not a finite parsable double.");
            }

            currentInput = newInput;
            value = newValue;
        }
        catch (NumberFormatException e) {
            throw new CalculatorInputException("'" + newInput + "' is not a parsable double.");
        }

        notifyListeners();
    }

    /**
     * Removes leading zeros from the string and returns the result.
     *
     * @param s the string to remove leading zeros from
     * @return input string without leading zeros
     */
    private String removeLeadingZeros(String s) {
        return s.replaceAll("^0+([0-9])", "$1");
    }

    @Override
    public boolean isActiveOperandSet() {
        return isActiveOperandSet;
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if (!isActiveOperandSet()) {
            throw new IllegalStateException("Active operand is not set");
        }
        
        return activeOperand;
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.isActiveOperandSet = true;
        this.activeOperand = activeOperand;
    }

    @Override
    public void clearActiveOperand() {
        isActiveOperandSet = false;
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return pendingOperation;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        pendingOperation = op;
    }

    @Override
    public String toString() {
        if (value == 0) {
            return isPositive ? "0" : "-0";
        }

        if (Double.isNaN(value)) {
            return "NaN";
        }

        if (Double.isInfinite(value)) {
            return (isPositive ? "" : "-") + "Infinity";
        }

        return (isPositive ? "" : "-") + currentInput;
    }
}
