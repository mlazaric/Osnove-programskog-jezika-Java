package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;
import hr.fer.zemris.java.custom.collections.List;

/**
 * Demo program for third homework.
 *
 * @author Marko Lazarić
 */
public class DemoList {

	public static void main(String[] args) {
		List col1 = new ArrayIndexedCollection();
		List col2 = new LinkedListIndexedCollection();

		col1.add("Ivana");
		col2.add("Jasna");

		Collection col3 = col1;
		Collection col4 = col2;

		col1.get(0);
		col2.get(0);
		//col3.get(0); // neće se prevesti! Razumijete li zašto? Jer Collection nema metodu get.
		//col4.get(0); // neće se prevesti! Razumijete li zašto?

		col1.forEach(System.out::println); // Ivana
		col2.forEach(System.out::println); // Jasna
		col3.forEach(System.out::println); // Ivana
		col4.forEach(System.out::println); // Jasna
	}

}
