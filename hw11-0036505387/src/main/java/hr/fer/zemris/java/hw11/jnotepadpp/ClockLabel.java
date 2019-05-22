package hr.fer.zemris.java.hw11.jnotepadpp;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class ClockLabel extends JLabel {

    private static final SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final int DELAY = 1000;

    private final SimpleDateFormat format;

    public ClockLabel(SimpleDateFormat format) {
        this.format = Objects.requireNonNull(format, "Format cannot be null.");

        JLabel clock = new JLabel("", JLabel.RIGHT);

        Timer timer = new Timer(DELAY, e -> updateClock());
        timer.setRepeats(true);
        timer.start();
    }

    public ClockLabel() {
        this(DEFAULT_FORMAT);
    }

    private void updateClock() {
        String text = format.format(Calendar.getInstance().getTime());

        setText(text);
    }

}
