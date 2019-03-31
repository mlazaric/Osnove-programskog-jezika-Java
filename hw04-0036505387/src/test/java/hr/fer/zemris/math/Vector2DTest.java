package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Vector2DTest {

	@Test
	void testConstructor() {
		var vector1 = new Vector2D(5, 10);
		var vector2 = new Vector2D(0, 0).translated(vector1);

		assertEquals(vector1, vector2);
	}

	@Test
	void testGetX() {
		assertEquals(5, new Vector2D(5, 10).getX());
	}

	@Test
	void testGetY() {
		assertEquals(10, new Vector2D(5, 10).getY());
	}

	@Test
	void testTranslate() {
		var vector = new Vector2D(5, 10);

		vector.translate(new Vector2D(-10, -20));

		assertEquals(new Vector2D(-5, -10), vector);
	}

	@Test
	void testTranslated() {
		assertEquals(new Vector2D(-5, -10), new Vector2D(5, 10).translated(new Vector2D(-10, -20)));
	}

	@Test
	void testRotate() {
		var vector = new Vector2D(5, 10);

		vector.rotate(Math.PI);

		assertEquals(new Vector2D(-5, -10), vector);
	}

	@Test
	void testRotated() {
		assertEquals(new Vector2D(-5, -10), new Vector2D(5, 10).rotated(Math.PI));
	}

	@Test
	void testScaleWithZero() {
		var vector = new Vector2D(5, 10);

		vector.scale(0);

		assertEquals(new Vector2D(0, 0), vector);
	}

	@Test
	void testScaleWithPositiveNumber() {
		var vector = new Vector2D(5, 10);

		vector.scale(5);

		assertEquals(new Vector2D(25, 50), vector);
	}

	@Test
	void testScaleWithNegativeNumber() {
		var vector = new Vector2D(-5, -10);

		vector.scale(5);

		assertEquals(new Vector2D(-25, -50), vector);
	}

	@Test
	void testScaledWithZero() {
		assertEquals(new Vector2D(0, 0), new Vector2D(5, 10).scaled(0));
	}

	@Test
	void testScaledWithPositiveNumber() {
		assertEquals(new Vector2D(25, 50), new Vector2D(5, 10).scaled(5));
	}

	@Test
	void testScaledWithNegativeNumber() {
		assertEquals(new Vector2D(-25, -50), new Vector2D(5, 10).scaled(-5));
	}

	@Test
	void testCopy() {
		var vector = new Vector2D(500, 300);

		assertEquals(vector, vector.copy());
	}

}
