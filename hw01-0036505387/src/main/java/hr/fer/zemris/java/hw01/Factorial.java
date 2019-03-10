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
	 *   <p>Continually prompts the user for an integer and calculates the factorial
	 * of the given number if it is within the accepted range. Exits when the user 
	 * inputs "kraj".</p>
	 *  
	 *   <p>If the user inputs an invalid number or an integer outside the accepted 
	 * range, a relevant message is printed to the console.</p>
	 * 
	 * @param args the arguments are ignored
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
	 *   <p>Calculates the factorial of the argument if it is within the accepted
	 * range.</p>
	 * 
	 * @param number number for which to calculate the factorial
	 * @return factorial of the number
	 * 
	 * @throws IllegalArgumentException if the number is not within the accepted
	 *                                  range [3, 20]
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
