package hr.fer.zemris.java.gui.calc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Models an invertible button that changes depending on the state of another element.
 *
 * @author Marko Lazarić
 */
@SuppressWarnings("serial")
class InvertibleButton extends JButton implements ItemListener, ActionListener {

    /**
     * The non inverted operation name.
     */
    private final String operationName;

    /**
     * Runs if the button is clicked and it is not inverted.
     */
    private final Runnable operation;

    /**
     * The inverted operation name.
     */
    private final String inverseOperationName;

    /**
     * Runs if the button is clicked and it is inverted.
     */
    private final Runnable inverseOperation;

    /**
     * Is it currently inverted?
     */
    private boolean isInverted;

    /**
     * Creates a new {@link InvertibleButton} with the given arguments.
     *
     * @param operationName the non inverted operation name
     * @param operation runs if the button is clicked and it is not inverted
     * @param inverseOperationName the inverted operation name
     * @param inverseOperation runs if the button is clicked and it is inverted
     */
    public InvertibleButton(String operationName, Runnable operation, String inverseOperationName, Runnable inverseOperation) {
        this.operationName = operationName;
        this.operation = operation;
        this.inverseOperationName = inverseOperationName;
        this.inverseOperation = inverseOperation;

        this.addActionListener(this);
        this.setText();
        this.setBackground(Calculator.BUTTON_BACKGROUND_COLOR);
    }

    /**
     * Sets the text depending on whether it is inverted or not.
     */
    private void setText() {
        if (isInverted) {
            setText(inverseOperationName);
        }
        else {
            setText(operationName);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isInverted) {
            inverseOperation.run();
        }
        else {
            operation.run();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        isInverted = e.getStateChange() == ItemEvent.SELECTED;
        setText();
    }
}
