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

}
