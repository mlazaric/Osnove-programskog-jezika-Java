package hr.fer.zemris.java.hw01;

import java.util.Scanner;

import hr.fer.zemris.java.hw01.PromptHelper.EndOfInputReachedException;

public class Factorial {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			try {
				int number = PromptHelper.promptInteger(sc, "broj");
				long factorial = factorial(number);
				
				System.out.format("%d! = %d%n", number, factorial);
			} 
			catch (IllegalArgumentException exc) {
				System.out.println(exc.getMessage());
			}
			catch (EndOfInputReachedException exc) {
				System.out.println(exc.getMessage());
				break;
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
