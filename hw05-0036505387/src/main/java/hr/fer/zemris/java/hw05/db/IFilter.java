package hr.fer.zemris.java.hw05.db;

@FunctionalInterface
public interface IFilter {

	boolean accepts(StudentRecord record);

}
