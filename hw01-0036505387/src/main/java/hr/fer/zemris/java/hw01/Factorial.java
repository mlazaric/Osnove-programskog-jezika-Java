package hr.fer.zemris.java.hw01;

import java.util.Scanner;

public class Factorial {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.print("Unesite broj > ");
			
			if (sc.hasNextInt()) {
				try {
					int number = sc.nextInt();
					long factorial = Factorial.factorial(number);
					
					System.out.format("%d! = %d%n", number, factorial);
				} catch (IllegalArgumentException exc) {
					System.out.println(exc.getMessage());
				}
			}
			else {
				String input = sc.next();
				
				if (input.toLowerCase().equals("kraj")) {
					System.out.println("Doviđenja.");
					break;
				}
				
				System.out.format("'%s' nije cijeli broj.%n", input);
			}
		}
		
		sc.close();
	}
	
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
