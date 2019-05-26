package hr.fer.zemris.java.custom.scripting.nodes;

import java.io.IOException;

public interface INodeVisitor {
    void visitTextNode(TextNode node);

    void visitForLoopNode(ForLoopNode node);

    void visitEchoNode(EchoNode node);

    void visitDocumentNode(DocumentNode node);
}