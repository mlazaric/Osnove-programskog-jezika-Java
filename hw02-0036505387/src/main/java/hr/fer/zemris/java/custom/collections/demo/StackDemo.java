package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * A demonstration program for the {@link ObjectStack}.
 * Uses the stack to parse postfix notation expressions.
 *
 * @author Marko Lazarić
 *
 */
public class StackDemo {

	/**
	 * Evaluates a postfix expression using {@link ObjectStack}. Every term and operator in the expression
	 * should be separated by one (or more) spaces. If it is not a valid expression or a wrong number of
	 * arguments has been passed to the program, a relevant error message will be printed and it will exit.
	 *
	 * @param args should contain a single {@link String} containing the postfix expression to be evaluated
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of arguments. Requires 1 argument.");

			return;
		}

		ObjectStack stack = new ObjectStack();
		String[] parts = args[0].split(" +");

		for (String part : parts) {
			try {
				Integer number = Integer.parseInt(part);

				stack.push(number);
			} catch (NumberFormatException e) {
				try {
					operate(stack, part.charAt(0));
				} catch(EmptyStackException | IllegalArgumentException exc) {
					System.out.println(e.getMessage());
					System.exit(1);
				}
			}
		}

		if (stack.size() != 1) {
			System.out.println("Invalid expression.");
			System.exit(1);
		}

		System.out.format("Expression evaluates to %d.", stack.pop());
	}

	/**
	 * Evaluates one operation. Takes two arguments from the stack and uses the
	 * passed operator on them and then pushes the result back on the stack.
	 *
	 * @param stack the stack used for parsing the postfix expression,
	 *               contains the numbers parsed and the result calculated
	 * @param operator one of the following charcters: +, -, *, / or %
	 *                                       (remainder of integer divison)
	 *
	 * @throws IllegalArgumentException if there aren't enough numbers on the stack
	 *                                  to evaluate the operator
	 *                                  
	 * @throws IllegalArgumentException if division by 0 is attempted 
	 * 
	 * @throws IllegalArgumentException if an unsupported operator is passed to
	 *                                  the function
	 */
	private static void operate(ObjectStack stack, char operator) {
		if (stack.size() < 2) {
			throw new IllegalArgumentException("Not enough numbers on the stack to evaluate the operator.");
		}

		int second = (int) stack.pop();
		int first = (int) stack.pop();

		switch (operator) {
			case '+':
				stack.push(first + second);
				break;
	
			case '-':
				stack.push(first - second);
				break;
	
			case '/':
				if (second == 0) {
					throw new IllegalArgumentException("Cannot divide by 0.");
				}
	
				stack.push(first / second);
				break;
	
			case '*':
				stack.push(first * second);
				break;
	
			case '%':
				stack.push(first % second);
				break;
	
			default:
				throw new IllegalArgumentException("Unsupported operator passed to the function.");
		}
	}

}
