package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * The top node representing a document in the syntax tree generated by {@link SmartScriptParser}.
 *
 * @author Marko Lazarić
 *
 */
public class DocumentNode extends Node {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		int numberOfChildren = numberOfChildren();

		for (int index = 0; index < numberOfChildren; index++) {
			Node child = getChild(index);

			sb.append(child.toString());
		}

		return sb.toString();
	}

	@Override
	public void accept(INodeVisitor iNodeVisitor) {
		iNodeVisitor.visitDocumentNode(this);
	}
}
