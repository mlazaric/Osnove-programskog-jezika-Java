package hr.fer.zemris.java.hw05.db;

import java.util.List;
import java.util.Objects;

public class QueryFilter implements IFilter {

	private final List<ConditionalExpression> query;

	public QueryFilter(List<ConditionalExpression> query) {
		this.query = Objects.requireNonNull(query, "List of conditional expression cannot be null.");
	}

	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression conditionalExpression : query) {
			boolean isSatisfied = conditionalExpression.getComparisonOperator().satisfied(
					conditionalExpression.getFieldGetter().get(record),
					conditionalExpression.getStringLiteral());

			if (!isSatisfied) {
				return false;
			}
		}

		return true;
	}

}
