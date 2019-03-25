package hr.fer.zemris.java.custom.collections.demo;

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
	 * Evaluates a postfix expression using {@link ObjectStack} passed as the one
	 * and only argument to the program. Every term and operator in the expression
	 * should be separated by one (or more) spaces. If it is not a valid expression
	 * or a wrong number of arguments has been passed to the program, a relevant
	 * error message will be printed and it will exit.
	 *
	 * @param args should contain a single {@link String} containing the postfix
	 * 				expression to be evaluated
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of arguments. Requires 1 argument.");

			return;
		}

		try {
			System.out.format("Expression evaluates to %d.", evaluatePostfixExpression(args[0]));
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Evaluates a postfix expression using {@link ObjectStack}. Every term
	 * and operator in the expression should be separated by one (or more) spaces.
	 * If it is not a valid expression, an {@link IllegalArgumentException} will
	 * be thrown.
	 *
	 * @param expression the postfix expression to be evaluated
	 *
	 * @throws IllegalArgumentException if the passed argument is not a valid
	 * 									postfix expression
	 */
	public static int evaluatePostfixExpression(String expression) {
		ObjectStack stack = new ObjectStack();
		String[] parts = expression.split(" +");

		for (String part : parts) {
			try {
				Integer number = Integer.parseInt(part);

				stack.push(number);
			} catch (NumberFormatException e) {
				if (part.length() != 1) {
					String message = String.format("Invalid operator: '%s'", part);

					throw new IllegalArgumentException(message);
				}

				operate(stack, part.charAt(0));
			}
		}

		if (stack.size() != 1) {
			throw new IllegalArgumentException("Invalid expression");
		}

		return (int) stack.pop();
	}

	/**
	 * Evaluates one operation. Takes two arguments from the stack and uses the
	 * passed operator on them and then pushes the result back on the stack.
	 *
	 * @param stack the stack used for parsing the postfix expression,
	 *               contains the numbers parsed and the result calculated
	 * @param operator one of the following characters: +, -, *, / or %
	 *                                       (remainder of integer division)
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
			if (second == 0) {
				throw new IllegalArgumentException("Cannot perform modulo 0.");
			}

			stack.push(first % second);
			break;

		default:
			throw new IllegalArgumentException("Unsupported operator passed to the function.");
		}
	}

}
