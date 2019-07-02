package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.*;
import java.awt.event.MouseListener;

public interface Tool extends MouseListener {

    /* mouse* methods are inherited from MouseListener */

    void paint(Graphics2D g2d);

}
