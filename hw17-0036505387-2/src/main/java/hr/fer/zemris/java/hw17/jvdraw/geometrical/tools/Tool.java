package hr.fer.zemris.java.hw17.jvdraw.geometrical.tools;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public interface Tool extends MouseListener, MouseMotionListener {

    /* mouse* methods are inherited from MouseListener */

    void paint(Graphics2D g2d);

    /* These are not offered by our API. */
    default void mousePressed(MouseEvent e) {}
    default void mouseEntered(MouseEvent e) {}
    default void mouseExited(MouseEvent e) {}

}
