package hr.fer.zemris.java.hw05.db;

@FunctionalInterface
public interface IFieldValueGetter {

	String get(StudentRecord record);

}
