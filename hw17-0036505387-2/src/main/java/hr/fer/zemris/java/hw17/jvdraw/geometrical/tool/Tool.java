package hr.fer.zemris.java.hw17.jvdraw.geometrical.tool;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * An abstract {@link Tool} which can draw {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject}s.
 *
 * @author Marko Lazarić
 */
public interface Tool extends MouseListener, MouseMotionListener {

    /* mouse* methods are inherited from MouseListener */

    /**
     * Paint the tool's current {@link hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject}.
     *
     * @param g2d the graphics object used for painting
     */
    void paint(Graphics2D g2d);

    /* These are not offered by our API. */
    default void mouseEntered(MouseEvent e) {}
    default void mouseExited(MouseEvent e) {}

}
