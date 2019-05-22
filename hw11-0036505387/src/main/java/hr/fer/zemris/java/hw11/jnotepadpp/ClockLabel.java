package hr.fer.zemris.java.hw11.jnotepadpp;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * Models a simple label which displays the time in the specified format and is updated every second.
 *
 * @author Marko Lazarić
 */
public class ClockLabel extends JLabel {

    /**
     * The default format for the date and time.
     */
    private static final SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * The delay between updates.
     */
    private static final int DELAY = 1000;

    /**
     * The format used for displaying the date and time.
     */
    private final SimpleDateFormat format;

    /**
     * Creates a {@link ClockLabel} with the specified parameter.
     *
     * @param format the format used for displaying the date and time
     */
    public ClockLabel(SimpleDateFormat format) {
        this.format = Objects.requireNonNull(format, "Format cannot be null.");

        Timer timer = new Timer(DELAY, e -> updateClock());
        timer.setRepeats(true);
        timer.start();
    }

    /**
     * Creates a {@link ClockLabel} with the {@link #DEFAULT_FORMAT}.
     */
    public ClockLabel() {
        this(DEFAULT_FORMAT);
    }

    /**
     * Updates the text of the label to the current date and time formatted using {@link #format}.
     */
    private void updateClock() {
        String text = format.format(Calendar.getInstance().getTime());

        setText(text);
    }

}
