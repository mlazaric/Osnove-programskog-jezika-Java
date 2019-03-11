package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RectangleTest {
	
	@Test
	public void testAreaWithValidDimensions() {
		double width = 5;
		double height = 7;
		
		assertEquals(Rectangle.calculateArea(width, height), width * height);
	}
	
	@Test
	public void testAreaWithNegativeWidth() {
		double width = -5;
		double height = 7;
		
		assertThrows(IllegalArgumentException.class, () -> Rectangle.calculateArea(width, height));
	}
	
	@Test
	public void testAreaWithNegativeHeight() {
		double width = 5;
		double height = -7;
		
		assertThrows(IllegalArgumentException.class, () -> Rectangle.calculateArea(width, height));
	}
	
	@Test
	public void testPerimeterWithValidDimensions() {
		double width = 5;
		double height = 7;
		
		assertEquals(Rectangle.calculatePerimeter(width, height), 2 * (width + height));
	}
	
	@Test
	public void testPerimeterWithNegativeWidth() {
		double width = -5;
		double height = 7;
		
		assertThrows(IllegalArgumentException.class, () -> Rectangle.calculatePerimeter(width, height));
	}
	
	@Test
	public void testPerimeterWithNegativeHeight() {
		double width = 5;
		double height = -7;
		
		assertThrows(IllegalArgumentException.class, () -> Rectangle.calculatePerimeter(width, height));
	}
	
	@Test
	public void testToStringWithValidDimensions() {
		double width = 2;
		double height = 8;
		
		String correctString = "Pravokutnik širine 2.0 i visine 8.0 ima površinu 16.0 te opseg 20.0.";
		
		assertEquals(Rectangle.toString(width, height), correctString);
	}
	
	@Test
	public void testToStringWithNegativeWidth() {
		double width = -5;
		double height = 7;
		
		assertThrows(IllegalArgumentException.class, () -> Rectangle.toString(width, height));
	}
	
	@Test
	public void testToStringWithNegativeHeight() {
		double width = 5;
		double height = -7;
		
		assertThrows(IllegalArgumentException.class, () -> Rectangle.toString(width, height));
	}
	
}