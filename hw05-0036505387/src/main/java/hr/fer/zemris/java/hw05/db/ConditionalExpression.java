package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Represents a single conditional expression comprised of an {@link IFieldValueGetter},
 * an {@link IComparisonOperator} and a {@link String} literal.
 *
 * @author Marko Lazarić
 *
 */
public class ConditionalExpression {

	/**
	 * The field getter for the field being compared.
	 */
	private final IFieldValueGetter fieldGetter;

	/**
	 * The string literal which is the second argument in {@link IComparisonOperator#satisfied(String, String)}.
	 */
	private final String stringLiteral;

	/**
	 * The comparison operator used in the expression.
	 */
	private final IComparisonOperator comparisonOperator;

	/**
	 * Creates a new {@link ConditionalExpression} with the given arguments.
	 *
	 * @param fieldGetter the field getter for the field being compared
	 * @param stringLiteral the string literal used as the second argument in the comparison
	 * @param comparisonOperator the comparison operator used in the expression
	 *
	 * @throws NullPointerException if any of the arguments is {@code null}
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
			IComparisonOperator comparisonOperator) {
		this.fieldGetter = Objects.requireNonNull(fieldGetter, "Field value getter cannot be null.");
		this.stringLiteral = Objects.requireNonNull(stringLiteral, "String literal cannot be null.");
		this.comparisonOperator = Objects.requireNonNull(comparisonOperator, "Comparison operator cannot be null.");
	}

	/**
	 * Returns the field getter for the field being compared.
	 *
	 * @return the field getter for the field being compared
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Returns the string literal used as the second argument in the comparison.
	 *
	 * @return the string literal used as the second argument in the comparison
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Returns the comparison operator used in the expression.
	 *
	 * @return the comparison operator used in the expression
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode() {
		return Objects.hash(comparisonOperator, fieldGetter, stringLiteral);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ConditionalExpression)) {
			return false;
		}

		ConditionalExpression other = (ConditionalExpression) obj;

		return Objects.equals(comparisonOperator, other.comparisonOperator)
				&& Objects.equals(fieldGetter, other.fieldGetter)
				&& Objects.equals(stringLiteral, other.stringLiteral);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "ConditionalExpression [fieldValueGetter=" + fieldGetter + ", stringLiteral=" + stringLiteral
				+ ", comparisonOperator=" + comparisonOperator + "]";
	}

}
