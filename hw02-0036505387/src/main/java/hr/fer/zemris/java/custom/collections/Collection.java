package hr.fer.zemris.java.custom.collections;

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
		Processor addAllProcessor = new Processor() {
			
			@Override
			public void process(Object value) {
				add(value);
			}
			
		};
		
		forEach(addAllProcessor);
	}
	
	public void clear() {}
	
}
