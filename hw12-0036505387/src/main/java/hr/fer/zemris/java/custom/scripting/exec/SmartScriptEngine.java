package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

/**
 * A class for executing a Smart Script document.
 *
 * @author Marko Lazarić
 */
public class SmartScriptEngine {
    /**
     * The smart script's {@link DocumentNode}.
     */
    private DocumentNode documentNode;

    /**
     * The {@link RequestContext} used for execution.
     */
    private RequestContext requestContext;

    /**
     * The {@link ObjectMultistack} used for execution.
     */
    private ObjectMultistack multistack = new ObjectMultistack();

    /**
     * Executes a Smart Script document using the {@link INodeVisitor} to visit all nodes.
     */
    private INodeVisitor visitor = new INodeVisitor() {

        @Override
        public void visitTextNode(TextNode node)  {
            try {
                requestContext.write(node.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            Number start = getNumberFromElementConstant(node.getStartExpression());
            Number end = getNumberFromElementConstant(node.getEndExpression());
            Number step = null;

            if (node.getStepExpression() == null) {
                step = 1;
            }
            else {
                step = getNumberFromElementConstant(node.getStepExpression());
            }

            String variable = node.getVariable().asText();

            multistack.push(variable, new ValueWrapper(start));

            while (multistack.peek(variable).numCompare(end) <= 0) {
                visitChildren(node);

                ValueWrapper current = multistack.pop(variable);

                current.add(step);

                multistack.push(variable, current);
            }

            multistack.pop(variable);
        }

        /**
         * Extracts a {@link Number} from {@link ElementConstantDouble} or {@link ElementConstantInteger}.
         *
         * @param element the element whose {@link Number} should be extracted
         * @return the extracted {@link Number}
         *
         * @throws RuntimeException if {@code element} is not a constant number element
         */
        private Number getNumberFromElementConstant(Element element) {
            if (element instanceof ElementConstantInteger) {
                return ((ElementConstantInteger) element).getValue();
            }
            else if (element instanceof ElementConstantDouble)
            {
                return ((ElementConstantDouble) element).getValue();
            }

            throw new RuntimeException("'" + element + "' is not a number element.");
        }

        @Override
        public void visitEchoNode(EchoNode node)  {
            Stack<ValueWrapper> valueStack = new Stack<>();

            for (Element element : node.getElements()) {
                if (element instanceof ElementConstantDouble) {
                    ValueWrapper value = new ValueWrapper(((ElementConstantDouble) element).getValue());

                    valueStack.push(value);
                }
                else if (element instanceof ElementConstantInteger) {
                    ValueWrapper value = new ValueWrapper(((ElementConstantInteger) element).getValue());

                    valueStack.push(value);
                }
                else if (element instanceof ElementString) {
                    ValueWrapper value = new ValueWrapper(((ElementString) element).getValue());

                    valueStack.push(value);
                }
                else if (element instanceof ElementVariable) {
                    String variable = ((ElementVariable) element).getName();

                    valueStack.push(multistack.peek(variable));
                }
                else if (element instanceof ElementOperator) {
                    String symbol = ((ElementOperator) element).getSymbol();
                    ValueWrapper first = valueStack.pop();
                    ValueWrapper second = valueStack.pop();

                    valueStack.push(Operators.applyOperator(symbol, first, second));
                }
                else if (element instanceof ElementFunction) {
                    String name = ((ElementFunction) element).getName();

                    Functions.applyFunction(requestContext, valueStack, name);
                }
            }

            for (ValueWrapper wrapper : valueStack) {
                try {
                    requestContext.write(wrapper.getValue().toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            visitChildren(node);
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
    };

    /**
     * Creates a new {@link SmartScriptEngine} with the given arguments.
     *
     * @param documentNode the smart script's {@link DocumentNode}
     * @param requestContext the {@link RequestContext} used for execution
     */
    public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
        this.documentNode = Objects.requireNonNull(documentNode, "Document node cannot be null.");
        this.requestContext = Objects.requireNonNull(requestContext, "Request context cannot be null.");
    }

    /**
     * Executes the Smart Script document.
     */
    public void execute() {
        documentNode.accept(visitor);
    }
}