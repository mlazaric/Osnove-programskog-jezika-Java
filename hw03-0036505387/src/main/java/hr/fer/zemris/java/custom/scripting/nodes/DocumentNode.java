package hr.fer.zemris.java.custom.scripting.nodes;

public class DocumentNode extends Node {
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
}
