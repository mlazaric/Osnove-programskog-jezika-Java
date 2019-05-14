package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CalcLayoutTest {

    static JPanel createAndAdd(Object constraint) {
        return createAndAdd(new JLabel("Test"), constraint);
    }

    static JPanel createAndAdd(Component component, Object constraint) {
        JPanel panel = new JPanel(new CalcLayout());

        panel.add(component, constraint);

        return panel;
    }

    @Test
    void testRowValues() {
        assertThrows(CalcLayoutException.class, () -> createAndAdd(new RCPosition(-1, 7)));
        assertThrows(CalcLayoutException.class, () -> createAndAdd(new RCPosition(0, 7)));
        assertThrows(CalcLayoutException.class, () -> createAndAdd(new RCPosition(6, 7)));
        assertThrows(CalcLayoutException.class, () -> createAndAdd(new RCPosition(7, 7)));


        for (int row = 1; row < 6; row++) {
            int currentRow = row; // local variables referenced from a lambda expression must be final or effectively final

            assertDoesNotThrow(() -> createAndAdd(new RCPosition(currentRow, 7)));
        }
    }

    @Test
    void testColumnValues() {
        assertThrows(CalcLayoutException.class, () -> createAndAdd(new RCPosition(2, -1)));
        assertThrows(CalcLayoutException.class, () -> createAndAdd(new RCPosition(2, 0)));
        assertThrows(CalcLayoutException.class, () -> createAndAdd(new RCPosition(2, 8)));
        assertThrows(CalcLayoutException.class, () -> createAndAdd(new RCPosition(2, 9)));


        for (int col = 1; col < 8; col++) {
            int currentCol = col; // local variables referenced from a lambda expression must be final or effectively final

            assertDoesNotThrow(() -> createAndAdd(new RCPosition(2, currentCol)));
        }
    }

    @Test
    void testFirstElementSpansFiveColumns() {
        assertDoesNotThrow(() -> createAndAdd(new RCPosition(1, 1)));

        for (int col = 2; col < 6; col++) {
            int currentCol = col; // local variables referenced from a lambda expression must be final or effectively final

            assertThrows(CalcLayoutException.class, () -> createAndAdd(new RCPosition(1, currentCol)));
        }
    }

    @Test
    void testMultipleComponentsUnderSameConstraint() {
        assertThrows(CalcLayoutException.class, () ->
                createAndAdd(new RCPosition(1, 1)).add(new JLabel("Test"), new RCPosition(1, 1)));
    }

    @Test
    void testPreferredSize1() {
        JPanel p = new JPanel(new CalcLayout(2));

        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));

        p.add(l1, new RCPosition(2,2));
        p.add(l2, new RCPosition(3,3));

        Dimension dim = p.getPreferredSize();

        assertEquals(new Dimension(152, 158), dim);
    }

    @Test
    void testPreferredSize2() {
        JPanel p = new JPanel(new CalcLayout(2));

        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));

        p.add(l1, new RCPosition(1,1));
        p.add(l2, new RCPosition(3,3));

        Dimension dim = p.getPreferredSize();

        assertEquals(new Dimension(152, 158), dim);
    }

    @Test
    void testRCPositionParsing() {
        JPanel p = new JPanel(new CalcLayout(3));

        p.add(new JLabel("x"), "1,1");
        p.add(new JLabel("y"), "2,3");
        p.add(new JLabel("z"), "2,7");
        p.add(new JLabel("w"), "4,2");
        p.add(new JLabel("a"), "4,5");
        p.add(new JLabel("b"), "4,7");
    }

}