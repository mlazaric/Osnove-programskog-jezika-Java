package hr.fer.zemris.java.hw05.db;

@FunctionalInterface
public interface IComparisonOperator {

	boolean satisfied(String value1, String value2);

}
