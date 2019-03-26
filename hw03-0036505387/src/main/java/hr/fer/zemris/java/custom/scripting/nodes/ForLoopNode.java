package hr.fer.zemris.java.custom.scripting.nodes;


import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

public class ForLoopNode extends Node {

	private final ElementVariable variable;
	private final Element startExpression;
	private final Element endExpression;
	private final Element stepExpression;

	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		this.variable = Objects.requireNonNull(variable, "Variable of a for loop cannot be null.");
		this.startExpression = Objects.requireNonNull(startExpression, "Start expression of a for loop cannot be null.");
		this.endExpression = Objects.requireNonNull(endExpression, "End expression of a for loop cannot be null.");
		this.stepExpression = stepExpression;
	}

	public ElementVariable getVariable() {
		return variable;
	}

	public Element getStartExpression() {
		return startExpression;
	}

	public Element getEndExpression() {
		return endExpression;
	}

	public Element getStepExpression() {
		return stepExpression;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{$ FOR ")
		.append(variable.asText())
		.append(' ')
		.append(startExpression.asText())
		.append(' ')
		.append(endExpression.asText());

		if (stepExpression != null) {
			sb.append(' ').append(stepExpression.asText());
		}

		sb.append(" $}");

		int numberOfChildren = numberOfChildren();

		for (int index = 0; index < numberOfChildren; index++) {
			Node child = getChild(index);

			sb.append(child.toString());
		}

		sb.append("{$END$}");

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();

		result = prime * result + Objects.hash(endExpression, startExpression, stepExpression, variable);

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof ForLoopNode)) {
			return false;
		}

		ForLoopNode other = (ForLoopNode) obj;

		return Objects.equals(endExpression, other.endExpression)
				&& Objects.equals(startExpression, other.startExpression)
				&& Objects.equals(stepExpression, other.stepExpression) && Objects.equals(variable, other.variable);
	}


}
