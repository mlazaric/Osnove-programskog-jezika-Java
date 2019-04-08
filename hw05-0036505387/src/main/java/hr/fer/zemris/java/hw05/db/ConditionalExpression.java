package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

public class ConditionalExpression {

	private final IFieldValueGetter fieldGetter;
	private final String stringLiteral;
	private final IComparisonOperator comparisonOperator;

	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
			IComparisonOperator comparisonOperator) {
		this.fieldGetter = Objects.requireNonNull(fieldGetter, "Field value getter cannot be null.");
		this.stringLiteral = Objects.requireNonNull(stringLiteral, "String literal cannot be null.");
		this.comparisonOperator = Objects.requireNonNull(comparisonOperator, "Comparison operator cannot be null.");
	}

	/**
	 * @return the fieldValueGetter
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * @return the stringLiteral
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * @return the comparisonOperator
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comparisonOperator, fieldGetter, stringLiteral);
	}

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

	@Override
	public String toString() {
		return "ConditionalExpression [fieldValueGetter=" + fieldGetter + ", stringLiteral=" + stringLiteral
				+ ", comparisonOperator=" + comparisonOperator + "]";
	}

}
