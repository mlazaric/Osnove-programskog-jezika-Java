package hr.fer.zemris.java.hw02;

import static hr.fer.zemris.java.hw02.ComplexNumber.fromImaginary;
import static hr.fer.zemris.java.hw02.ComplexNumber.fromMagnitudeAndAngle;
import static hr.fer.zemris.java.hw02.ComplexNumber.fromReal;
import static hr.fer.zemris.java.hw02.ComplexNumber.parse;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ComplexNumberTest {

	static ComplexNumber[] numbers = new ComplexNumber[] {
			ComplexNumber.ONE,
			ComplexNumber.MINUS_ONE,
			ComplexNumber.ZERO,
			ComplexNumber.I,
			ComplexNumber.MINUS_I,

			fromReal(5),
			fromImaginary(Math.sqrt(10)),
			new ComplexNumber(-115, 124),
			fromMagnitudeAndAngle(0, Math.PI),

			new ComplexNumber(1, 1),
			new ComplexNumber(1, -1),
			new ComplexNumber(-1, 1),
			new ComplexNumber(-1, -1)
	};

	static double[] realParts = new double[] {
			1,
			-1,
			0,
			0,
			0,

			5,
			0,
			-115,
			0,

			1,
			1,
			-1,
			-1
	};

	static double[] imaginaryParts = new double[] {
			0,
			0,
			0,
			1,
			-1,

			0,
			Math.sqrt(10),
			124,
			0,

			1,
			-1,
			1,
			-1
	};

	static double[] magnitudes = new double[] {
			1,
			1,
			0,
			1,
			1,

			5,
			Math.sqrt(10),
			Math.sqrt(115 * 115 + 124 * 124),
			0,

			Math.sqrt(2),
			Math.sqrt(2),
			Math.sqrt(2),
			Math.sqrt(2)
	};

	static double[] angles = new double[] {
			0,
			Math.PI,
			0,
			Math.PI / 2,
			3 * Math.PI / 2,

			0,
			Math.PI / 2,
			2.31855537,
			Math.PI,

			Math.PI / 4,
			7 * Math.PI / 4,
			3 * Math.PI / 4,
			5 * Math.PI / 4
	};

	@Test
	void testConstructor() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(numbers[index], new ComplexNumber(realParts[index], imaginaryParts[index]));
		}
	}

	@Test
	void testFromReal() {
		assertEquals(ComplexNumber.ONE, fromReal(1));
		assertEquals(ComplexNumber.ZERO, fromReal(0));
		assertEquals(ComplexNumber.MINUS_ONE, fromReal(-1));

		assertEquals(new ComplexNumber(5, 0), fromReal(5));
		assertEquals(new ComplexNumber(-5, 0), fromReal(-5));
		assertEquals(new ComplexNumber(0, 0), fromReal(0));
	}

	@Test
	void testFromImaginary() {
		assertEquals(ComplexNumber.I, fromImaginary(1));
		assertEquals(ComplexNumber.ZERO, fromImaginary(0));
		assertEquals(ComplexNumber.MINUS_I, fromImaginary(-1));

		assertEquals(new ComplexNumber(0, 5), fromImaginary(5));
		assertEquals(new ComplexNumber(0, -5), fromImaginary(-5));
		assertEquals(new ComplexNumber(0, 0), fromImaginary(0));
	}

	@Test
	void testFromMagnitudeAndAngleWithKnownNumbers() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(numbers[index],
					fromMagnitudeAndAngle(magnitudes[index], angles[index]));
		}
	}

	@Test
	void testFromMagnitudeAndAngleOnGetters() {
		for (ComplexNumber number : numbers) {
			assertEquals(number,
					fromMagnitudeAndAngle(number.getMagnitude(), number.getAngle()));
		}
	}

	@Test
	void testParseWithOnlyRealPart() {
		assertEquals(fromReal(351), parse("351"));
		assertEquals(fromReal(-317), parse("-317"));
		assertEquals(fromReal(3.51), parse("3.51"));
		assertEquals(fromReal(-3.17), parse("-3.17"));
	}

	@Test
	void testParseWithOnlyImaginaryPart() {
		assertEquals(fromImaginary(351), parse("351i"));
		assertEquals(fromImaginary(-317), parse("-317i"));
		assertEquals(fromImaginary(3.51), parse("3.51i"));
		assertEquals(fromImaginary(-3.17), parse("-3.17i"));
	}

	@Test
	void testParseWithOnlyI() {
		assertEquals(ComplexNumber.I, parse("i"));
		assertEquals(ComplexNumber.MINUS_I, parse("-i"));
	}

	@Test
	void testParseWithIBeforeImaginaryPart() {
		assertThrows(IllegalArgumentException.class, () -> parse("i351"));
		assertThrows(IllegalArgumentException.class, () -> parse("-i317"));
		assertThrows(IllegalArgumentException.class, () -> parse("i3.51"));
		assertThrows(IllegalArgumentException.class, () -> parse("-i3.17"));
	}

	@Test
	void testParseWithRealAndImaginaryPart() {
		assertEquals(new ComplexNumber(-2.71, -3.15), parse("-2.71-3.15i"));
		assertEquals(new ComplexNumber(31, 24), parse("31+24i"));
		assertEquals(new ComplexNumber(-1, -1), parse("-1-i"));
	}

	@Test
	void testParseWithMultipleSigns() {
		assertThrows(IllegalArgumentException.class, () -> parse("-+2.71"));
		assertThrows(IllegalArgumentException.class, () -> parse("--2.71"));
		assertThrows(IllegalArgumentException.class, () -> parse("-2.71+-3.15i"));
	}

	@Test
	void testParseWithLeadingPlus() {
		assertEquals(fromReal(2.71), parse("+2.71"));
		assertEquals(new ComplexNumber(2.71, 3.15), parse("+2.71+3.15i"));
		assertEquals(ComplexNumber.I, parse("+i"));
	}

	@Test
	void testParseWithToString() {
		for (ComplexNumber number : numbers) {
			assertEquals(number, parse(number.toString()));
		}
	}

	@Test
	void testParseWithMultipleComplexNumbers() {
		assertThrows(IllegalArgumentException.class, () -> parse("2i+3i"));
		assertThrows(IllegalArgumentException.class, () -> parse("2+3"));
		assertThrows(IllegalArgumentException.class, () -> parse("1+i-3+i"));
	}

	@Test
	void testParseWithBlankStrings() {
		assertThrows(IllegalArgumentException.class, () -> parse(""));
		assertThrows(IllegalArgumentException.class, () -> parse("  "));
		assertThrows(IllegalArgumentException.class, () -> parse(" +  "));
	}

	@Test
	void testGetReal() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(realParts[index], numbers[index].getReal(), ComplexNumber.THRESHOLD);
		}
	}

	@Test
	void testGetImaginary() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(imaginaryParts[index], numbers[index].getImaginary(), ComplexNumber.THRESHOLD);
		}
	}

	@Test
	void testGetMagnitude() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(magnitudes[index], numbers[index].getMagnitude(), ComplexNumber.THRESHOLD);
		}
	}

	@Test
	void testGetAngle() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(angles[index], numbers[index].getAngle(), ComplexNumber.THRESHOLD);
		}
	}

	@Test
	void testConjugate() {
		assertEquals(ComplexNumber.ZERO, ComplexNumber.ZERO.conjugate());
		assertEquals(ComplexNumber.ONE, ComplexNumber.ONE.conjugate());
		assertEquals(ComplexNumber.MINUS_ONE, ComplexNumber.MINUS_ONE.conjugate());
		assertEquals(ComplexNumber.MINUS_I, ComplexNumber.I.conjugate());
		assertEquals(new ComplexNumber(53, 63), new ComplexNumber(53, -63).conjugate());
	}

	@Test
	void testAddWithZero() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(numbers[index], numbers[index].add(ComplexNumber.ZERO));
		}
	}

	@Test
	void testAddKnownNumbers() {
		assertEquals(new ComplexNumber(1, -1), ComplexNumber.ONE.add(ComplexNumber.MINUS_I));
		assertEquals(ComplexNumber.ZERO, ComplexNumber.ONE.add(ComplexNumber.MINUS_ONE));
		assertEquals(new ComplexNumber(50 + 13, 25 - 18), new ComplexNumber(50, 25).add(new ComplexNumber(13, -18)));
	}

	@Test
	void testSubWithZero() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(numbers[index], numbers[index].sub(ComplexNumber.ZERO));
		}
	}

	@Test
	void testSubKnownNumbers() {
		assertEquals(new ComplexNumber(1, 1), ComplexNumber.ONE.sub(ComplexNumber.MINUS_I));
		assertEquals(fromReal(2), ComplexNumber.ONE.sub(ComplexNumber.MINUS_ONE));
		assertEquals(new ComplexNumber(50 - 13, 25 + 18), new ComplexNumber(50, 25).sub(new ComplexNumber(13, -18)));
	}

	@Test
	void testMulWithScalar() {
		assertEquals(fromReal(5), ComplexNumber.ONE.mul(5));
		assertEquals(ComplexNumber.MINUS_ONE, ComplexNumber.ONE.mul(-1));
		assertEquals(new ComplexNumber(-25 * 13,  25 * 16),
				new ComplexNumber(13, -16).mul(-25));
	}

	@Test
	void testMulWithOne() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(numbers[index], numbers[index].mul(ComplexNumber.ONE));
		}
	}

	@Test
	void testMulWithKnownNumbers() {
		assertEquals(fromImaginary(-1), ComplexNumber.ONE.mul(ComplexNumber.MINUS_I));
		assertEquals(fromReal(-1), ComplexNumber.ONE.mul(ComplexNumber.MINUS_ONE));
		assertEquals(new ComplexNumber(50 * 13 + 25 * 18, -50 * 18 + 25 * 13),
				new ComplexNumber(50, 25).mul(new ComplexNumber(13, -18)));
	}

	@Test
	void testDivWithScalar() {
		assertEquals(fromReal(1/5.0), ComplexNumber.ONE.div(5));
		assertEquals(ComplexNumber.MINUS_ONE, ComplexNumber.ONE.div(-1));
		assertEquals(new ComplexNumber(13,  -16),
				new ComplexNumber(-25 * 13, 25 * 16).div(-25));
	}

	@Test
	void testDivWithOne() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(numbers[index], numbers[index].div(ComplexNumber.ONE));
		}
	}

	@Test
	void testDivWithKnownNumbers() {
		assertEquals(fromImaginary(1), ComplexNumber.ONE.div(ComplexNumber.MINUS_I));
		assertEquals(fromReal(-1), ComplexNumber.ONE.div(ComplexNumber.MINUS_ONE));
		assertEquals(new ComplexNumber(200.0/493, 1225.0/493),
				new ComplexNumber(50, 25).div(new ComplexNumber(13, -18)));
	}

	@Test
	void testPowerWithOne() {
		for (int n = 0; n < 10; ++n) {
			assertEquals(ComplexNumber.ONE, ComplexNumber.ONE.power(n));
		}
	}

	@Test
	void testPowerWithKnownNumbers() {
		assertEquals(ComplexNumber.MINUS_I, ComplexNumber.I.power(163));
		assertEquals(new ComplexNumber(-506864, 1236480), new ComplexNumber(3,  5).power(8));
	}

	@Test
	void testPowerWithZeroAsExponent() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(ComplexNumber.ONE, numbers[index].power(0));
		}
	}

	@Test
	void testPowerWithNegativeExponent() {
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.MINUS_ONE.power(-1));
	}

	@Test
	void testFirstRoot() {
		for (int index = 0; index < numbers.length; index++) {
			assertEquals(numbers[index], numbers[index].root(1)[0]);
		}
	}

	@Test
	void testNegativeRoot() {
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.MINUS_ONE.root(-1));
	}

	@Test
	void testZeroRoot() {
		assertThrows(IllegalArgumentException.class, () -> ComplexNumber.MINUS_ONE.root(0));
	}

	@Test
	void testKnownRoots() {
		assertArrayEquals(new ComplexNumber[] {
				ComplexNumber.I,
				ComplexNumber.MINUS_I
		}, ComplexNumber.MINUS_ONE.root(2));

		assertArrayEquals(new ComplexNumber[] {
				ComplexNumber.ONE,
				ComplexNumber.I,
				ComplexNumber.MINUS_ONE,
				ComplexNumber.MINUS_I
		}, ComplexNumber.ONE.root(4));

		assertArrayEquals(new ComplexNumber[] {
				new ComplexNumber(1.05857815, 0.16766230),
				new ComplexNumber(0.16766230, 1.05857815),
				new ComplexNumber(-0.95495715, 0.48657496),
				new ComplexNumber(-0.75785828, -0.75785828),
				new ComplexNumber(0.48657496, -0.9549571)
		}, new ComplexNumber(1, 1).root(5));
	}

}
