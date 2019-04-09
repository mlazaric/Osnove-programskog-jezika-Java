package hr.fer.zemris.lsystems.impl.demo;

import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Simple demonstration program for the first task of the fifth homework.
 *
 * @author Marko Lazarić
 */
public class Glavni3 {

	public static void main(String[] args) {
		LSystemViewer.showLSystem(LSystemBuilderImpl::new);
	}

}
