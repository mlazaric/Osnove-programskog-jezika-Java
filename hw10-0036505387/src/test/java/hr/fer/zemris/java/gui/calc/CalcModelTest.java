package hr.fer.zemris.java.gui.calc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.function.DoubleBinaryOperator;

public class CalcModelTest {
	
	private CalcModel model;
	
	private static CalcModel newCalcModel() {
		// Zamijenite ovo tako da vraća primjerak Vaše implementacije modela.
		return new CalcModelImpl();
	}

	@BeforeEach
	public void setup() {
		model = newCalcModel();
	}

	@Test
	public void valueOfNewModel() {
		assertEquals(0.0, model.getValue(), 1E-10); 
	}
	
	@Test
	public void toStringOfNewModel() {
		assertEquals("0", model.toString()); 
	}

	@Test
	public void newModelIsEditable() {
		assertTrue(model.isEditable()); 
	}

	@Test
	public void valueOfNewModelAfterSignSwap() {
		model.swapSign();
		
		assertEquals(0.0, model.getValue(), 1E-10); 
	}
	
	@Test
	public void toStringOfNewModelAfterSignSwap() {
		model.swapSign();
		
		assertEquals("-0", model.toString()); 
	}

	@Test
	public void pointWhenNoNumberThrows() {
		assertThrows(CalculatorInputException.class, ()->{
			model.insertDecimalPoint();
		});
	}

	@Test
	public void pointWhenNoNumberAndNegativeSignThrows() {
		model.swapSign();

		assertThrows(CalculatorInputException.class, ()->{model.insertDecimalPoint();});
	}

	@Test
	public void setSimpleValue() {
		model.setValue(-3.14);

		assertEquals(-3.14, model.getValue(), 1E-10); 
	}

	@Test
	public void toStringAfterSetSimpleValue() {
		model.setValue(-3.14);

		assertEquals("-3.14", model.toString());
	}

	@Test
	public void afterSetValueModelIsNotEditable() {
		model.setValue(-3.14);

		assertFalse(model.isEditable());
	}

	@Test
	public void setInfinityValue() {
		model.setValue(Double.POSITIVE_INFINITY);

		assertEquals(Double.POSITIVE_INFINITY, model.getValue()); 
	}

	@Test
	public void toStringAfterSetInfinityValue() {
		model.setValue(Double.POSITIVE_INFINITY);

		assertEquals("Infinity", model.toString()); 
	}

	@Test
	public void toStringAfterSetNegativeInfinityValue() {
		model.setValue(Double.NEGATIVE_INFINITY);

		assertEquals("-Infinity", model.toString()); 
	}

	@Test
	public void enterWholeNumber() {
		model.insertDigit(1);
		model.insertDigit(1);
		model.insertDigit(9);

		assertEquals(119, model.getValue(), 1E-10); 
		assertEquals("119", model.toString()); 
	}

	@Test
	public void enterDecimalNumber() {
		model.insertDigit(1);
		model.insertDigit(1);
		model.insertDigit(9);
		model.insertDecimalPoint();
		model.insertDigit(3);
		model.insertDigit(2);

		assertEquals(119.32, model.getValue(), 1E-10); 
		assertEquals("119.32", model.toString()); 
	}

	@Test
	public void checkDecimalNumberWithZeroWhole() {
		model.insertDigit(0);
		model.insertDecimalPoint();
		model.insertDigit(0);
		model.insertDigit(3);
		model.insertDigit(0);
		model.insertDigit(2);

		assertEquals(0.0302, model.getValue(), 1E-10); 
		assertEquals("0.0302", model.toString()); 
	}

	@Test
	public void signHasEffectOnZeroValue() {
		model.insertDigit(0);
		model.swapSign();

		assertEquals(0.0, model.getValue(), 1E-10); 
		assertEquals("-0", model.toString()); 
	}

	@Test
	public void whenSwapSingIsLast() {
		model.insertDigit(5);
		model.swapSign();

		assertEquals(-5, model.getValue(), 1E-10); 
		assertEquals("-5", model.toString()); 
	}

	@Test
	public void evenNumberOfSwapSignHasNoEffect() {
		model.insertDigit(5);
		model.swapSign();
		model.swapSign();
		model.swapSign();
		model.swapSign();

		assertEquals(5.0, model.getValue(), 1E-10); 
		assertEquals("5", model.toString()); 
	}

	@Test
	public void swapSignWorkAtAnyTime() {
		model.insertDigit(0);
		model.insertDecimalPoint();
		model.insertDigit(3);
		model.swapSign();
		model.insertDigit(2);

		assertEquals(-0.32, model.getValue(), 1E-10); 
		assertEquals("-0.32", model.toString()); 
	}

	@Test
	public void multipleInsertDecimalPointThrowsException() {
		model.insertDigit(4);
		model.insertDecimalPoint();
		model.insertDigit(3);
		
		assertThrows(CalculatorInputException.class, ()->{
			model.insertDecimalPoint();
		});
	}

	// Pokušaj dodavanja nove znamenke kojom bi broj postao prevelik
	// za konačnu reprezentaciju u double-tipu se ne dopušta već baca
	// iznimku!
	@Test
	public void safeWithTooBigNumbers() {
		for(int i = 1; i <= 308; i++) {
			model.insertDigit(9);
		}

		assertThrows(CalculatorInputException.class, ()->{
			model.insertDigit(9);
		});
	}
	
	@Test()
	public void readingActiveOperandWhenNotSet() {
		assertThrows(IllegalStateException.class, ()->{
			model.getActiveOperand();
		});
	}
	
	@Test
	public void setActiveOperand() {
		double value = 42.0;
		model.setActiveOperand(value);
		assertTrue(model.isActiveOperandSet());
		assertEquals(42.0, model.getActiveOperand(), 1E-10); 
	}

	@Test
	public void afterClearActiveOperandActiveOperandIsNotSet() {
		model.setActiveOperand(42);
		model.clearActiveOperand();
		assertFalse(model.isActiveOperandSet());
	}
	
	@Test
	public void activeOperandIsInitiallyNotSet() {
		assertFalse(model.isActiveOperandSet());
	}

	@Test
	public void afterClearAllActiveOperandIsNotSet() {
		model.setActiveOperand(42);
		model.clearAll();
		assertFalse(model.isActiveOperandSet());
	}
	
	@Test
	public void afterClearActiveOperandRemainsSet() {
		model.setActiveOperand(42);
		model.clear();
		assertTrue(model.isActiveOperandSet());
	}

	@Test
	public void multipleZerosStartingNumberAreIgnored() {
		model.insertDigit(0);
		model.insertDigit(0);
		model.insertDigit(0);
		model.insertDigit(0);
		
		assertEquals(0.0, model.getValue(), 1E-10); 
		assertEquals("0", model.toString()); 
	}

	@Test
	public void leadingZerosAreIgnored() {
		model.insertDigit(0);
		model.insertDigit(0);
		model.insertDigit(3);
		model.insertDigit(4);
		
		assertEquals(34.0, model.getValue(), 1E-10); 
		assertEquals("34", model.toString()); 
	}

	@Test
	public void exampleFromHomeworkAssignment() {
		model.insertDigit(5);
		model.insertDigit(8);
		model.setActiveOperand(model.getValue());
		model.setPendingBinaryOperation(Double::sum);
		model.clear();
		model.insertDigit(1);
		model.insertDigit(4);
		
		double result = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
		model.setValue(result);
		model.clearActiveOperand();
		model.setPendingBinaryOperation(null);
		
		assertEquals(72.0, model.getValue(), 1E-10); 
		assertEquals("72.0", model.toString()); 
	}

	private static class DummyCalcModel implements CalcModel {

		@Override
		public void addCalcValueListener(CalcValueListener l) {
			notimp();
		}

		@Override
		public void removeCalcValueListener(CalcValueListener l) {
			notimp();
		}

		@Override
		public boolean isEditable() {
			notimp();
			return false;
		}
		
		@Override
		public double getValue() {
			notimp();
			return 0;
		}
		
		@Override
		public void setValue(double value) {
			notimp();
		}

		@Override
		public void clear() {
			notimp();
		}

		@Override
		public void clearAll() {
			notimp();
		}
		
		@Override
		public void swapSign() {
			notimp();
		}

		@Override
		public void insertDecimalPoint() {
			notimp();
		}

		@Override
		public void insertDigit(int digit) {
			notimp();
		}

		@Override
		public boolean isActiveOperandSet() {
			notimp();
			return false;
		}

		@Override
		public double getActiveOperand() {
			notimp();
			return 0;
		}

		@Override
		public void setActiveOperand(double activeOperand) {
			notimp();
		}

		@Override
		public void clearActiveOperand() {
			notimp();
		}
		
		@Override
		public DoubleBinaryOperator getPendingBinaryOperation() {
			notimp();
			return null;
		}
		
		@Override
		public void setPendingBinaryOperation(DoubleBinaryOperator op) {
			notimp();
		}
		
		private void notimp() {
			throw new UnsupportedOperationException("Method is not implemented yet!");
		}
	}
}
