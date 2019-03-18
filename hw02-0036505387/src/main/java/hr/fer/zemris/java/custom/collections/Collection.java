package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

public class Collection {
	
	protected Collection() {}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public int size() {
		return 0;
	}
	
	public void add(Object value) {}
	
	public boolean contains(Object value) {
		return false;
	}
	
	public boolean remove(Object value) {
		return false;
	}
	
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	public void forEach(Processor processor) {}
	
	public void addAll(Collection other) {
		Objects.requireNonNull(other);
		
		class AddAllProcessor extends Processor {
			
			@Override
			public void process(Object value) {
				add(value);
			}
			
		}
		
		other.forEach(new AddAllProcessor());
	}
	
	public void clear() {}
	
}
