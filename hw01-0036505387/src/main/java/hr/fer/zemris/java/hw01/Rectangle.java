package hr.fer.zemris.java.hw01;

import java.util.Scanner;

public class Rectangle {

	public static void main(String[] args) {
		if (args.length == 2) {
			try {
				double width = Double.parseDouble(args[0]);
				double height = Double.parseDouble(args[1]);
				
				Rectangle rect = new Rectangle(width, height);
				
				System.out.println(rect);
			} catch (NumberFormatException e) {
				System.out.format("'%s' ili '%s' se ne može protumačiti kao broj%n", args[0], args[1]);
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
			
			return;
		}
		
		if (args.length != 0) {
			System.out.format("Neispravan broj argumenata, program prima 0 ili 2 argumenata.");
			
			return;
		}
		
		Scanner sc = new Scanner(System.in);
		Prompt prompt = new Prompt();
		
		try {
			double width = prompt.promptPositiveDouble(sc, "širinu");
			double height = prompt.promptPositiveDouble(sc, "visinu");
			
			Rectangle rect = new Rectangle(width, height);
			
			System.out.println(rect);
		} 
		catch (IllegalArgumentException exc) {
			System.out.println(exc.getMessage());
		}
		
		sc.close();
	}
	
	private final double width, height;
	
	public Rectangle(double width, double height) {
		if (width <= 0) {
			throw new IllegalArgumentException("Širine mora biti veća od 0.");
		}
		if (height <= 0) {
			throw new IllegalArgumentException("Visina mora biti veća od 0.");
		}
		
		this.width = width;
		this.height = height;
	}
	
	public double getArea() {
		return width * height;
	}
	
	public double getPerimeter() {
		return 2 * (width + height);
	}
	
	public String toString() {
		return String.format("Pravokutnik širine %.1f i visine %.1f ima površinu %.1f te opseg %.1f.",
				               width, height, getArea(), getPerimeter());
	}
}
