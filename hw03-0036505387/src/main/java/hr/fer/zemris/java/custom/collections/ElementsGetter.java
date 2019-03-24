package hr.fer.zemris.java.custom.collections;

public interface ElementsGetter {

	boolean hasNextElement();

	Object getNextElement();

	default void processRemaining(Processor p) {
		while (hasNextElement()) {
			p.process(getNextElement());
		}
	}

}
