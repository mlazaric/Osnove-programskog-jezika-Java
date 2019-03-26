package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * Demo program for third homework.
 *
 * @author Marko Lazarić
 */
public class Demo3 {

	public static void main(String[] args) {
		// Test the demo code with both an ArrayIndexedCollection and a LinkedListIndexedCollection
		try {
			demo3(new ArrayIndexedCollection());
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();

		try {
			demo3(new LinkedListIndexedCollection());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void demo3(Collection col) {
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		ElementsGetter getter = col.createElementsGetter();

		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
	}

}
