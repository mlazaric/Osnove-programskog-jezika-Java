package hr.fer.zemris.java.hw01;

import java.util.Scanner;

public class Rectangle {

	public static void main(String[] args) {
		if (args.length == 2) {
			try {
				double width = Double.parseDouble(args[0]);
				double height = Double.parseDouble(args[1]);
				
				System.out.println(toString(width, height));
			} catch (NumberFormatException e) {
				System.out.format("'%s' ili '%s' se ne može protumačiti kao broj%n", args[0], args[1]);
			}
			
			return;
		}
		
		if (args.length != 0) {
			System.out.format("Neispravan broj argumenata, program prima 0 ili 2 argumenata.");
			
			return;
		}
		
		Scanner sc = new Scanner(System.in);
		
		double[] dimensions = new double[2];
		String[] dimensionNames = {"visinu", "širinu"};
		
		for (int i = 0; i < dimensionNames.length; i++) {
			while (true) {
				System.out.format("Unesite %s > ", dimensionNames[i]);
				
				String token = sc.next();
				
				try {
					dimensions[i] = Double.parseDouble(token);
					
					if (dimensions[i] > 0) {
						break;
					}
					
					System.out.println("Unijeli ste negativnu vrijednost.");
					
				}
				catch (NumberFormatException e) {
					System.out.format("'%s' se ne može protumačiti kao broj.%n", token);
				}
			}
		}
		
		System.out.println(toString(dimensions[0], dimensions[1]));
		
		sc.close();
	}
	
	public static double getArea(double width, double height) {
		return width * height;
	}
	
	public static double getPerimeter(double width, double height) {
		return 2 * (width + height);
	}
	
	public static String toString(double width, double height) {
		return String.format("Pravokutnik širine %.1f i visine %.1f ima površinu %.1f te opseg %.1f.",
				               width, height, getArea(width, height), getPerimeter(width, height));
	}
}
