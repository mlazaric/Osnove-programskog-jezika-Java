package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * Demo program for the first task of third homework.
 *
 * @author Marko Lazarić
 */
public class Demo1 {

	public static void main(String[] args) {
		// Test the demo code with both an ArrayIndexedCollection and a LinkedListIndexedCollection
		try {
			demo1(new ArrayIndexedCollection());
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();

		try {
			demo1(new LinkedListIndexedCollection());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void demo1(Collection col) {
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		ElementsGetter getter = col.createElementsGetter();

		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
	}

}
