package hr.fer.zemris.java.custom.collections.demo;

import java.util.Iterator;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

/**
 * A simple demonstration program for {@link SimpleHashtable}.
 */
public class SimpleHashtableDemo4 {

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

		System.out.println(examMarks);

		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();

			if(pair.getKey().equals("Ivana")) {
				iter.remove(); // sam iterator kontrolirano uklanja trenutni element
			}
		}

		System.out.println(examMarks);
	}

}
