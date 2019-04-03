package hr.fer.zemris.java.custom.collections.demo;

import java.util.Iterator;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

/**
 * A simple demonstration program for {@link SimpleHashtable}.
 */
public class SimpleHashtableDemo7 {

	public static void main(String[] args) {
		// create collection:
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);

		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();

		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();

			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());

			iter.remove();
		}

		System.out.printf("Veličina: %d%n", examMarks.size());
	}

}
