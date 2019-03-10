package hr.fer.zemris.java.hw01;

import java.util.Scanner;

public class PromptHelper {
	
	public static int promptInteger(Scanner sc, String name) {
		while (true) {
			System.out.format("Unesite %s > ", name);
			
			if (sc.hasNextInt()) {
				return sc.nextInt();
			}
			
			String input = sc.next();
			
			if (input.toLowerCase().equals("kraj")) {
				throw new EndOfInputReachedException("Doviđenja");
			}
			
			System.out.format("'%s' nije cijeli broj.%n", input);
		}
	}
	
	public static double promptDouble(Scanner sc, String name) {
		while (true) {
			System.out.format("Unesite %s > ", name);
			
			if (sc.hasNextDouble()) {
				return sc.nextDouble();
			}
			
			String input = sc.next();
			
			if (input.toLowerCase().equals("kraj")) {
				throw new EndOfInputReachedException("Doviđenja");
			}
			
			System.out.format("'%s' se ne može protumačiti kao broj.%n", input);
		}
	}
	
	@SuppressWarnings("serial")
	public static class EndOfInputReachedException extends RuntimeException {
		public EndOfInputReachedException(String string) {
			super(string);
		}
	}
}
