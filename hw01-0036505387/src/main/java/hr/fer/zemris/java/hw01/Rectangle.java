package hr.fer.zemris.java.hw01;

import java.util.Scanner;


/**
 * A program for calculating the area and the perimeter of a rectangle.
 *
 * @author Marko Lazarić
 *
 */
public class Rectangle {

	/**
	 * <p>If 0 arguments are given to the program, it prompts the user for the width
	 * and the height of the rectangle. It only accepts valid numbers greater than 0.
	 * It will print a relevant error message for any invalid input and prompt again.
	 * When the user as entered two valid numbers, it will print the dimensions, the
	 * area and the perimeter of the rectangle.</p>
	 *
	 * <p>If 2 arguments are given to the program, it will use those as the width and
	 * the height of the rectangle. If they are not a valid number or they are not
	 * greater than 0, it will print a relevant error message to the console and exit.
	 * If they are both valid dimensions, it will print the dimensions, the area and
	 * the perimeter of the rectangle.</p>
	 *
	 * <p>If any other number of arguments are given to the program, it will print a
	 * relevant error message to the console and exit.</p>
	 *
	 * @param args the arguments from the description
	 */
	public static void main(String[] args) {
		if (args.length == 2) {
			try {
				double width = Double.parseDouble(args[0]);
				double height = Double.parseDouble(args[1]);

				System.out.println(toString(width, height));
			}
			catch (NumberFormatException e) {
				System.out.format("'%s' ili '%s' se ne može protumačiti kao broj.%n", args[0], args[1]);
			}
			catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}

			return;
		}

		if (args.length != 0) {
			System.out.format("Neispravan broj argumenata, program prima 0 ili 2 argumenata.%n");

			return;
		}

		Scanner sc = new Scanner(System.in);

		double width = promptArgument("širina", sc);
		double height = promptArgument("visina", sc);

		System.out.println(toString(width, height));

		sc.close();
	}

	/**
	 * Prompts the user for a single positive real number.
	 * If the user enters an invalid number or a number lesser than
	 * or equal to 0, a relevant error message is printed to the user.
	 *
	 * @param name the name of the argument
	 * @param sc scanner instance
	 * @return the value of the argument
	 */
	public static double promptArgument(String name, Scanner sc) {
		while (true) {
			System.out.format("Unesite %s > ", name);

			String token = sc.next();

			try {
				double argument = Double.parseDouble(token);

				if (argument > 0) {
					return argument;
				}

				System.out.println("Unijeli ste negativnu vrijednost.");

			}
			catch (NumberFormatException e) {
				System.out.format("'%s' se ne može protumačiti kao broj.%n", token);
			}
		}
	}

	/**
	 * Calculates the area of a rectangle with the given dimensions.
	 *
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @return area of the rectangle
	 *
	 * @throws IllegalArgumentException if either the width or the height is
	 *                                   less than or equal to 0
	 */
	public static double calculateArea(double width, double height) {
		if (width <= 0) {
			throw new IllegalArgumentException("Širina mora biti veća od 0.");
		}
		if (height <= 0) {
			throw new IllegalArgumentException("Visina mora biti veća od 0.");
		}

		return width * height;
	}

	/**
	 * Calculates the perimeter of a rectangle with the given dimensions.
	 *
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @return perimeter of the rectangle
	 *
	 * @throws IllegalArgumentException if either the width or the height is
	 *                                   less than or equal to 0
	 */
	public static double calculatePerimeter(double width, double height) {
		if (width <= 0) {
			throw new IllegalArgumentException("Širina mora biti veća od 0.");
		}
		if (height <= 0) {
			throw new IllegalArgumentException("Visina mora biti veća od 0.");
		}

		return 2 * (width + height);
	}

	/**
	 * Describes the rectangle with the given width and height.
	 *
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @return a string describing the dimensions, the area and the perimeter of the rectangle
	 *
	 * @see #calculateArea(double, double)
	 * @see #calculatePerimeter(double, double)
	 */
	public static String toString(double width, double height) {
		return String.format("Pravokutnik širine %.1f i visine %.1f ima površinu %.1f te opseg %.1f.",
				width, height, calculateArea(width, height), calculatePerimeter(width, height));
	}
}
