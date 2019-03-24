package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

public class Demo5 {

	public static void main(String[] args) {
		try {
			demo6(new ArrayIndexedCollection());
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();

		try {
			demo6(new LinkedListIndexedCollection());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void demo6(Collection col) {
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		ElementsGetter getter = col.createElementsGetter();

		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());

		col.clear();

		System.out.println("Jedan element: " + getter.getNextElement());
	}

}
