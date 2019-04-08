package hr.fer.zemris.java.hw05.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.hw05.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw05.db.lexer.Token;
import hr.fer.zemris.java.hw05.db.lexer.TokenType;

public class QueryParser {

	private static final Map<String, IComparisonOperator> OPERATORS;
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

	private final QueryLexer lexer;
	private final List<ConditionalExpression> query;
	private boolean hasDirect;

	public QueryParser(String input) {
		lexer = new QueryLexer(input);
		query = new LinkedList<>();

		parseQuery();
	}

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

			if (lexer.getToken().getType() != TokenType.WORD || !lexer.getToken().getValue().equals("and")) {
				throw new IllegalArgumentException("'" + lexer.getToken().getValue() + "' is not a valid part of a query.");
			}
		}
	}

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

	public boolean isDirectQuery() {
		return hasDirect && query.size() == 1;
	}

	public String getQueriedJMBAG() {
		if (!isDirectQuery()) {
			throw new IllegalStateException("Cannot get query JMBAG from indirect query.");
		}

		return query.get(0).getStringLiteral();
	}

	public List<ConditionalExpression> getQuery() {
		return query;
	}

}
