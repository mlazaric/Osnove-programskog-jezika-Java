package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Tester;

/**
 * Demo program for third homework.
 *
 * @author Marko Lazarić
 */
public class DemoTester1 {

	public static void main(String[] args) {
		Tester t = new EvenIntegerTester();

		System.out.println(t.test("Ivo"));
		System.out.println(t.test(22));
		System.out.println(t.test(3));
	}

}
