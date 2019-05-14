package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import static java.lang.Math.abs;

public class CalcModelImpl implements CalcModel {

    private boolean isEditable = true;
    private boolean isPositive = true;
    private String currentInput = "";
    private double value = 0;
    private double activeOperand;
    private DoubleBinaryOperator pendingOperation;
    private boolean isActiveOperandSet = false;

    private List<CalcValueListener> listeners = new ArrayList<>();

    private void notifyListeners() {
        for (CalcValueListener listener : listeners) {
            listener.valueChanged(this);
        }
    }

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        // Copy on write to prevent any concurrent modification exceptions.
        listeners = new ArrayList<>(listeners);

        listeners.add(l);
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
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
