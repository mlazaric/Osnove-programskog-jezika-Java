package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Demonstration program for using {@link INodeVisitor}.
 *
 * @author Marko Lazarić
 */
public class TreeWriter {

    /**
     * Parses a smart script file and then it turns the parsed tree into text and prints it to System.out.
     *
     * @param args expects one argument, the path to the file to parse and print
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Program expects one argument: path to the file to parse.");
            return;
        }

        Path path = Paths.get(args[0]);

        if (!Files.isReadable(path)) {
            System.out.println("File '" + path.toString() + "' is not readable.");
            return;
        }

        String docBody = null;

        try {
            docBody = Files.readString(path);
        } catch (SmartScriptParserException e) {
            System.out.println(e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("Error occurred while reading file.");
            return;
        }

        SmartScriptParser p = new SmartScriptParser(docBody);
        WriterVisitor visitor = new WriterVisitor();
        p.getDocumentNode().accept(visitor);
    }

    /**
     * Writes smart script document to System.out.
     *
     * @author Marko Lazarić
     */
    private static class WriterVisitor implements INodeVisitor {

        private final StringBuilder output = new StringBuilder();

        @Override
        public void visitTextNode(TextNode node) {
            output.append(node.getText());
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            output.append("{$ FOR ")
                  .append(node.getVariable().asText())
                  .append(' ')
                  .append(node.getStartExpression().asText())
                  .append(' ')
                  .append(node.getEndExpression().asText());

            if (node.getStepExpression() != null) {
                output.append(' ').append(node.getStepExpression().asText());
            }

            output.append(" $}");

            visitChildren(node);

            output.append("{$ END $}");
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            output.append("{$=");

            for (Element element : node.getElements()) {
                output.append(element.asText()).append(' ');
            }

            output.append("$}");
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            visitChildren(node);

            System.out.println(output.toString());
        }

        /**
         * Visits all children of a node.
         *
         * @param node the node whose children should be visited
         */
        private void visitChildren(Node node) {
            int numberOfChildren = node.numberOfChildren();

            for (int index = 0; index < numberOfChildren; ++index) {
                node.getChild(index).accept(this);
            }
        }

    }
}
