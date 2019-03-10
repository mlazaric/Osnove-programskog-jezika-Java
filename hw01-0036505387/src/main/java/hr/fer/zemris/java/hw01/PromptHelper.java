package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * A helper class for prompting the user for a specific type of value.
 * 
 * @author Marko Lazarić
 *
 */
public class PromptHelper {
	
	/**
	 *   <p>Continually prompts the user for a valid integer. If the user has entered
     * a valid integer, the integer is returned. Otherwise a helpful message is
     * printed to the console and the user is prompted again.</p>
     * 
     *   <p>If the user enters "kraj", an exception is thrown to indicated that the
     * end of the input stream has been reached.</p>
	 *   
	 * @param sc instance of {@link Scanner}, used for input
	 * @param name name of the attribute being prompted
	 * @return the first valid integer entered by the user
	 * 
	 * @throws EndOfInputReachedException when the user inputs "kraj".
	 */
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
	
	public static double promptPositiveDouble(Scanner sc, String name) {
		while (true) {
			System.out.format("Unesite %s > ", name);
			
			String input = sc.next();
			
			try {
				double value = Double.parseDouble(input);
				
				if (value < 0) {
					System.out.println("Unijeli ste negativnu vrijednost.");
				}
			} catch (NumberFormatException e) {
				if (input.toLowerCase().equals("kraj")) {
					throw new EndOfInputReachedException("Doviđenja");
				}
				
				System.out.format("'%s' se ne može protumačiti kao broj.%n", input);
			}
		}
	}
	
	/**
	 * An exception used to indicate the user wants to close the prompt.
	 * 
	 * @author Marko Lazarić
	 *
	 */
	@SuppressWarnings("serial")
	public static class EndOfInputReachedException extends RuntimeException {
		public EndOfInputReachedException(String message) {
			super(message);
		}
	}
}
