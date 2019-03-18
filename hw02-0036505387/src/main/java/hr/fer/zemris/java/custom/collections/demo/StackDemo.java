package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ObjectStack;

public class StackDemo {

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
				operate(stack, part.charAt(0));
			}
		}

		if (stack.size() != 1) {
			System.out.println("Invalid expression.");
			System.exit(1);
		}

		System.out.format("Expression evaluates to %d.", stack.pop());
	}

	public static void operate(ObjectStack stack, char operator) {
		if (stack.size() < 2) {
			System.out.println("Invalid expression.");

			System.exit(1);
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
				System.out.println("Error: Division by zero.");
				System.exit(1);
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
			System.out.format("Invalid operator '%c'.%n", operator);
			System.exit(1);
			break;
		}
	}

}
