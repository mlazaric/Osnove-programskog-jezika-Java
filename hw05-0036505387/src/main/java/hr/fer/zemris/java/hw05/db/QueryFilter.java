package hr.fer.zemris.java.hw05.db;

import java.util.List;
import java.util.Objects;

/**
 * An {@link IFilter} representing the parsed query.
 *
 * @author Marko Lazarić
 *
 */
public class QueryFilter implements IFilter {

	/**
	 * The list of {@link ConditionalExpression}s parsed from the textual query.
	 */
	private final List<ConditionalExpression> query;

	/**
	 * Creates a new {@link QueryFilter} with the given attributes.
	 *
	 * @param query the list of {@link ConditionalExpression}s parsed from the textual query.
	 *
	 * @throws NullPointerException if {@code query} is {@code null}
	 */
	public QueryFilter(List<ConditionalExpression> query) {
		this.query = Objects.requireNonNull(query, "List of conditional expression cannot be null.");
	}


	/**
	 * Returns whether the record satisfies all the {@link ConditionalExpression}s comprising the query.
	 *
	 * @return true if it satisfies all the {@link ConditionalExpression}s comprising the query,
	 *         false otherwise
	 */
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
