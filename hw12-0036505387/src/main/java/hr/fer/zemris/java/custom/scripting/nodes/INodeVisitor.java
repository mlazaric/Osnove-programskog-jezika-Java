package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Interface for implementing the visitor pattern for visiting all nodes in a parsed syntax tree of a smart script.
 *
 * @author Marko Lazarić
 */
public interface INodeVisitor {

    /**
     * Visit a {@link TextNode}.
     *
     * @param node the {@link TextNode} to visit
     */
    void visitTextNode(TextNode node);

    /**
     * Visit a {@link ForLoopNode}.
     *
     * @param node the {@link ForLoopNode} to visit
     */
    void visitForLoopNode(ForLoopNode node);

    /**
     * Visit a {@link EchoNode}.
     *
     * @param node the {@link EchoNode} to visit
     */
    void visitEchoNode(EchoNode node);

    /**
     * Visit a {@link DocumentNode}.
     *
     * @param node the {@link DocumentNode} to visit
     */
    void visitDocumentNode(DocumentNode node);

}