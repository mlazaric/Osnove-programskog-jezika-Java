package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LSystemBuilderImplTest {

	@Test
	void testFromPDF() {
		var lSystemBuilder = new LSystemBuilderImpl();

		lSystemBuilder.setAxiom("F").registerProduction('F', "F+F--F+F");

		var lSystem = lSystemBuilder.build();

		assertEquals("F", lSystem.generate(0));
		assertEquals("F+F--F+F", lSystem.generate(1));
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", lSystem.generate(2));
	}

}
