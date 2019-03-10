package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RectangleTest {
	
	@Test
	public void testNegativeWidth() {
		assertThrows(IllegalArgumentException.class, () -> new Rectangle(-1, 5));
	}
	
	@Test
	public void testNegativeHeight() {
		assertThrows(IllegalArgumentException.class, () -> new Rectangle(5, -1));
	}
	
	@Test
	public void testZeroWidth() {
		assertThrows(IllegalArgumentException.class, () -> new Rectangle(0, 5));
	}
	
	@Test
	public void testZeroHeight() {
		assertThrows(IllegalArgumentException.class, () -> new Rectangle(5, 0));
	}
	
	@Test
	public void testArea() {
		double width = 5;
		double height = 7;
		
		Rectangle rect = new Rectangle(width, height);
		
		assertEquals(rect.getArea(), width * height);
	}
	
	@Test
	public void testPerimeter() {
		double width = 5;
		double height = 7;
		
		Rectangle rect = new Rectangle(width, height);
		
		assertEquals(rect.getPerimeter(), 2 * (width + height));
	}
	
	@Test
	public void testToString() {
		double width = 2;
		double height = 8;
		
		Rectangle rect = new Rectangle(width, height);
		String correctString = "Pravokutnik širine 2.0 i visine 8.0 ima površinu 16.0 te opseg 20.0.";
		
		assertEquals(rect.toString(), correctString);
	}
}