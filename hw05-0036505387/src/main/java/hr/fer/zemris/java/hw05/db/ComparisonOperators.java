package hr.fer.zemris.java.hw05.db;

public final class ComparisonOperators {

	private ComparisonOperators() {}

	public static final IComparisonOperator LESS = (s1, s2) -> s1.compareTo(s2) < 0;
	public static final IComparisonOperator EQUALS = (s1, s2) -> s1.compareTo(s2) == 0;
	public static final IComparisonOperator GREATER = (s1, s2) -> s1.compareTo(s2) > 0;

	public static final IComparisonOperator LESS_OR_EQUALS = (s1, s2) -> !GREATER.satisfied(s1, s2);
	public static final IComparisonOperator GREATER_OR_EQUALS = (s1, s2) -> !LESS.satisfied(s1, s2);
	public static final IComparisonOperator NOT_EQUALS = (s1, s2) -> !EQUALS.satisfied(s1, s2);

	public static final IComparisonOperator LIKE = new LikeComparisonOperator();

	private static class LikeComparisonOperator implements IComparisonOperator {

		@Override
		public boolean satisfied(String value1, String value2) {
			int numberOfStars = value2.length() - value2.replace("*", "").length();

			if (numberOfStars != 1) {
				throw new IllegalArgumentException("Expression '" + value2 + "' contains more than one *.");
			}

			if (value2.endsWith("*")) {
				// Check if value1 starts with value2 without the * suffix.
				return value1.startsWith(value2.substring(0, value2.length() - 1));
			}

			if (value2.startsWith("*")) {
				// Check if value1 end with value2 without the * prefix.
				return value1.endsWith(value2.substring(1));
			}

			String parts[] = value2.split("\\*");

			// Check prefix, suffix and make sure they do not overlap by checking the length of the first value.
			return value1.startsWith(parts[0]) && value1.endsWith(parts[1]) && value1.length() >= (parts[0].length() + parts[1].length());
		}

	}
}
