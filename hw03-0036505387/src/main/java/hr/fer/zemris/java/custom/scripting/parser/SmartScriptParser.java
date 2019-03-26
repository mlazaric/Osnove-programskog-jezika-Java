package hr.fer.zemris.java.custom.scripting.parser;

import java.util.Arrays;
import java.util.regex.Pattern;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.List;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerState;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptToken;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

public class SmartScriptParser {

	private SmartScriptLexer lexer;
	private DocumentNode documentNode;

	private static final Pattern VARIABLE_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9_]*$");
	private static final Pattern FUNCTION_PATTERN = Pattern.compile("^@[A-Za-z][A-Za-z0-9_]*$");
	private static final Pattern TAG_PATTERN = Pattern.compile("^(=|[A-Za-z][A-Za-z0-9_]*)$");

	public SmartScriptParser(String toParse) {
		lexer = new SmartScriptLexer(toParse);

		parse();
	}

	private void parse() {
		ObjectStack stack = new ObjectStack();

		stack.push(new DocumentNode());

		while (true) {
			SmartScriptToken token = lexer.nextToken();

			if (token.getType() == SmartScriptTokenType.EOF) {
				break;
			}
			else if (token.getType() == SmartScriptTokenType.TEXT) {
				((Node) stack.peek()).addChildNode(new TextNode((String) token.getValue()));
			}
			else if (token.getType() == SmartScriptTokenType.TAG_START) {
				lexer.setState(SmartScriptLexerState.INSIDE_TAG);
			}
			else if (token.getType() == SmartScriptTokenType.IDENTIFIER) {
				String identifier = (String) token.getValue();

				if (identifier.equals("=")) {
					((Node) stack.peek()).addChildNode(parseEchoTag(stack));
				}
				else if (identifier.equalsIgnoreCase("for")) {
					ForLoopNode node = parseForLoop();

					((Node) stack.peek()).addChildNode(node);
					stack.push(node);
				}
				else if (identifier.equalsIgnoreCase("end")) {
					if (stack.isEmpty()) {
						throw new SmartScriptParserException("Invalid expression: too many ends");
					}

					stack.pop();

					SmartScriptToken nextToken = lexer.nextToken();

					if (nextToken.getType() != SmartScriptTokenType.TAG_END) {
						throw new SmartScriptParserException("Invalid end tag");
					}
				}
				else {
					String message = String.format("Invalid identifier '%s'.", identifier);

					throw new SmartScriptParserException(message);
				}

				lexer.setState(SmartScriptLexerState.BASIC);
			}
			else {
				String message = String.format("Invalid token '%s'", token.getValue());

				throw new SmartScriptParserException(message);
			}
		}

		if (stack.size() != 1) {
			throw new SmartScriptParserException("Invalid number of end tags.");
		}

		documentNode = (DocumentNode) stack.pop();
	}

	private EchoNode parseEchoTag(ObjectStack stack) {
		Collection collection = new ArrayIndexedCollection();

		while (true) {
			SmartScriptToken token = lexer.nextToken();

			if (token.getType() == SmartScriptTokenType.EOF) {
				throw new SmartScriptParserException("Echo tag was not closed.");
			}
			else if (token.getType() == SmartScriptTokenType.TAG_END) {
				break;
			}
			else if (token.getType() == SmartScriptTokenType.DOUBLE) {
				collection.add(new ElementConstantDouble((Double) token.getValue()));
			}
			else if (token.getType() == SmartScriptTokenType.INTEGER) {
				collection.add(new ElementConstantInteger((Integer) token.getValue()));
			}
			else if (token.getType() == SmartScriptTokenType.OPERATOR) {
				collection.add(new ElementOperator((String) token.getValue()));
			}
			else if (token.getType() == SmartScriptTokenType.STRING) {
				collection.add(new ElementString((String) token.getValue()));
			}
			else if (token.getType() == SmartScriptTokenType.IDENTIFIER) {
				String identifier = (String) token.getValue();

				if (FUNCTION_PATTERN.matcher(identifier).find()) {
					collection.add(new ElementFunction(identifier.substring(1)));
				}
				else if (VARIABLE_PATTERN.matcher(identifier).find()) {
					collection.add(new ElementVariable(identifier));
				}
				else {
					String message = String.format("Invalid identifier '%s'.", identifier);

					throw new SmartScriptParserException(message);
				}
			}
			else {
				throw new SmartScriptParserException("Unknown token in echo tag.");
			}
		}

		return new EchoNode(Arrays.copyOf(collection.toArray(), collection.size(), Element[].class));
	}

	private ForLoopNode parseForLoop() {
		List collection = new ArrayIndexedCollection();

		while (true) {
			SmartScriptToken token = lexer.nextToken();

			if (token.getType() == SmartScriptTokenType.EOF) {
				throw new SmartScriptParserException("For loop tag was not closed.");
			}
			else if (token.getType() == SmartScriptTokenType.TAG_END) {
				break;
			}
			else if (token.getType() == SmartScriptTokenType.DOUBLE) {
				collection.add(new ElementConstantDouble((Double) token.getValue()));
			}
			else if (token.getType() == SmartScriptTokenType.INTEGER) {
				collection.add(new ElementConstantInteger((Integer) token.getValue()));
			}
			else if (token.getType() == SmartScriptTokenType.STRING) {
				collection.add(new ElementString((String) token.getValue()));
			}
			else if (token.getType() == SmartScriptTokenType.IDENTIFIER) {
				String identifier = (String) token.getValue();

				if (VARIABLE_PATTERN.matcher(identifier).find()) {
					collection.add(new ElementVariable(identifier));
				}
				else {
					String message = String.format("Invalid identifier '%s'.", identifier);

					throw new SmartScriptParserException(message);
				}
			}
			else {
				String message = String.format("Unknown '%s' in for loop tag.", (String) token.getValue());

				throw new SmartScriptParserException(message);
			}
		}

		if (collection.size() < 3 || collection.size() > 4) {
			throw new SmartScriptParserException("Invalid number of arguments in the for loop tag.");
		}
		if (!(collection.get(0) instanceof ElementVariable)) {
			throw new SmartScriptParserException("First argument of for loop tag has to be a variable.");
		}

		if (collection.size() == 3) {
			return new ForLoopNode((ElementVariable) collection.get(0), (Element) collection.get(1), (Element) collection.get(2), null);
		}

		return new ForLoopNode((ElementVariable) collection.get(0), (Element) collection.get(1), (Element) collection.get(2), (Element) collection.get(3));
	}

	public DocumentNode getDocumentNode() {
		return documentNode;
	}
}
