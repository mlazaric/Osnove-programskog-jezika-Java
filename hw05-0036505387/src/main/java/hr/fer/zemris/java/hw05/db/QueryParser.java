package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.hw05.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw05.db.lexer.Token;
import hr.fer.zemris.java.hw05.db.lexer.TokenType;

/**
 * Query parser for the simple {@link StudentDatabase}.
 *
 * @author Marko Lazarić
 *
 */
public class QueryParser {

	/**
	 * The string to comparison operator mapping.
	 */
	private static final Map<String, IComparisonOperator> OPERATORS;

	/**
	 * The string to field value getter mapping.
	 */
	private static final Map<String, IFieldValueGetter> FIELDS;

	static {
		OPERATORS = new HashMap<>();

		OPERATORS.put("<", ComparisonOperators.LESS);
		OPERATORS.put("=", ComparisonOperators.EQUALS);
		OPERATORS.put(">", ComparisonOperators.GREATER);

		OPERATORS.put("<=", ComparisonOperators.LESS_OR_EQUALS);
		OPERATORS.put("!=", ComparisonOperators.NOT_EQUALS);
		OPERATORS.put(">=", ComparisonOperators.GREATER_OR_EQUALS);

		OPERATORS.put("LIKE", ComparisonOperators.LIKE);

		FIELDS = new HashMap<>();

		FIELDS.put("jmbag", FieldValueGetters.JMBAG);
		FIELDS.put("firstName", FieldValueGetters.FIRST_NAME);
		FIELDS.put("lastName", FieldValueGetters.LAST_NAME);
	}

	/**
	 * The lexer used for tokenising the input.
	 */
	private final QueryLexer lexer;

	/**
	 * The list of {@link ConditionalExpression}s representing the query.
	 */
	private final List<ConditionalExpression> query;

	/**
	 * Whether it has a {@link ConditionalExpression} where JMBAG is being compared with the equals operator.
	 * Used in {@link #isDirectQuery()}.
	 */
	private boolean hasDirect;

	/**
	 * Creates a new {@link QueryParser} with the given arguments.
	 *
	 * @param input the string to parse
	 *
	 * @throws NullPointerException if {@code input} is {@code null}
	 */
	public QueryParser(String input) {
		lexer = new QueryLexer(input);
		query = new ArrayList<>();

		parseQuery();
	}

	/**
	 * Parses the query string and generates a list of {@link ConditionalExpression}s.
	 *
	 * @throws IllegalArgumentException if it is not a valid query
	 */
	private void parseQuery() {
		while (true) {
			ConditionalExpression expression = parseConditionalExpression();

			query.add(expression);

			if (expression.getComparisonOperator() == ComparisonOperators.EQUALS &&
					expression.getFieldGetter() == FieldValueGetters.JMBAG) {
				hasDirect = true;
			}

			if (lexer.nextToken().getType() == TokenType.EOF) {
				break;
			}

			if (lexer.getToken().getType() != TokenType.WORD || !lexer.getToken().getValue().equalsIgnoreCase("and")) {
				throw new IllegalArgumentException("'" + lexer.getToken().getValue() + "' is not a valid part of a query.");
			}
		}
	}

	/**
	 * Parses a single {@link ConditionalExpression}.
	 *
	 * @return the parsed {@link ConditionalExpression}
	 *
	 * @throws IllegalArgumentException if an invalid operator or field name is given
	 */
	private ConditionalExpression parseConditionalExpression() {
		Token identifierToken = lexer.nextToken();
		Token operatorToken = lexer.nextToken();
		Token stringLiteralToken = lexer.nextToken();

		checkTokenTypes(identifierToken, operatorToken, stringLiteralToken);

		IComparisonOperator operator = OPERATORS.get(operatorToken.getValue());

		if (operator == null) {
			throw new IllegalArgumentException("'" + operatorToken.getValue() + "' is not a valid operator.");
		}

		IFieldValueGetter getter = FIELDS.get(identifierToken.getValue());

		if (getter == null) {
			throw new IllegalArgumentException("'" + identifierToken.getValue() + "' is not a field name.");
		}

		return new ConditionalExpression(getter, stringLiteralToken.getValue(), operator);
	}

	/**
	 * Check the order of the token types. <br/>
	 * <br/>
	 * The first token should be a {@link TokenType#WORD} containing the field name. <br/>
	 * The second token should be a {@link TokenType#OPERATOR} or {@link TokenType#WORD} (LIKE operator). <br/>
	 * The third token should be a {@link TokenType#STRING}.
	 *
	 * @param identifier the field name
	 * @param operator the operator
	 * @param stringLiteral the string literal
	 *
	 * @throws IllegalArgumentException if the {@link TokenType}s do not match
	 */
	private void checkTokenTypes(Token identifier, Token operator, Token stringLiteral) {
		if (identifier.getType() != TokenType.WORD) {
			throw new IllegalArgumentException("First argument of conditional expression should be an identifier.");
		}

		if (operator.getType() != TokenType.OPERATOR && operator.getType() != TokenType.WORD) {
			throw new IllegalArgumentException("Second argument of conditional expression should be an operator.");
		}

		if (stringLiteral.getType() != TokenType.STRING) {
			throw new IllegalArgumentException("Third argument of conditional expression should be a string literal.");
		}
	}

	/**
	 * Returns whether this is a direct query and can be run in O(1).
	 *
	 * @return whether this is a direct query
	 */
	public boolean isDirectQuery() {
		return hasDirect && query.size() == 1;
	}

	/**
	 * If this is a direct query, returns the queried JMBAG. Otherwise it throws.
	 *
	 * @return the queried JMBAG
	 *
	 * @throws IllegalStateException if it is not a direct query
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery()) {
			throw new IllegalStateException("Cannot get query JMBAG from indirect query.");
		}

		return query.get(0).getStringLiteral();
	}

	/**
	 * Returns a list of {@link ConditionalExpression}s representing the query.
	 *
	 * @return a list of {@link ConditionalExpression}s representing the query
	 */
	public List<ConditionalExpression> getQuery() {
		return query;
	}

}
