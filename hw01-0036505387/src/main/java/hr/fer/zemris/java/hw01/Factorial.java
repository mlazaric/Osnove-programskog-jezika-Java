package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * A program for calculating the factorial of a number.
 * 
 * @author Marko Lazarić
 *
 */
public class Factorial {

	/**
	 *   Continually prompts the user for an integer. If the user has entered a 
	 * valid integer within the accepted interval, it prints the factorial of the 
	 * entered number. Otherwise it prints a relevant error message to the console.
	 * 
	 * @param args the arguments passed to the program are ignored
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.print("Unesite broj > ");
			
			if (sc.hasNextInt()) {
				int number = sc.nextInt();
				
				try {
					long factorial = factorial(number);
					
					System.out.format("%d! = %d%n", number, factorial);
				} 
				catch (IllegalArgumentException exc) {
					System.out.println(exc.getMessage());
				}
				
				continue;
			}
			
			String token = sc.next();
			
			if (token.equals("kraj")) {
				System.out.println("Doviđenja.");
				
				break;
			}
			
			System.out.format("'%s' nije cijeli broj.%n", token);
		}
		
		sc.close();
	}
	
	/**
	 *   If the argument is within the accepted range (3 <= number <= 20), it
	 * returns the factorial of the argument. Otherwise it throws an exception.
	 * 
	 * @param number number for which to calculate the factorial
	 * @return factorial of the number
	 * 
	 * @throws IllegalArgumentException if the number is not within the accepted
	 *                                  range (3 <= number <= 20)
	 */
	public static long factorial(int number) {
		if (number < 3 || number > 20) {
			String message = String.format("'%d' nije broj u dozvoljenom rasponu.", number);
			
			throw new IllegalArgumentException(message);
		}
		
		long factorial = 1;
		
		for (int i = 1; i <= number; ++i) {
			factorial *= i;
		}
		
		return factorial;
	}
}
