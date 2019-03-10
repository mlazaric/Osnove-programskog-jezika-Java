package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RectangleTest {
	
	@Test
	public void testArea() {
		double width = 5;
		double height = 7;
		
		assertEquals(Rectangle.getArea(width, height), width * height);
	}
	
	@Test
	public void testPerimeter() {
		double width = 5;
		double height = 7;
		
		assertEquals(Rectangle.getPerimeter(width, height), 2 * (width + height));
	}
	
	@Test
	public void testToString() {
		double width = 2;
		double height = 8;
		
		String correctString = "Pravokutnik širine 2.0 i visine 8.0 ima površinu 16.0 te opseg 20.0.";
		
		assertEquals(Rectangle.toString(width, height), correctString);
	}
	
}