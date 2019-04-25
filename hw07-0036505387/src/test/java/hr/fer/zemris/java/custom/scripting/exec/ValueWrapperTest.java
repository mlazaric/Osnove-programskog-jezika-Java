package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueWrapperTest {

    static final Object[] INVALID_TYPES = {
            Boolean.TRUE,
            new Object(),
            Character.valueOf('3'),
            Byte.valueOf((byte) 5),
            Long.valueOf(23),
            "ABCDSASDd"
    };

    static final Object[] VALID_TYPES = {
            Double.valueOf(3.14),
            Math.PI,
            Math.E,
            Integer.valueOf(13),
            "15",
            "-13.2",
            "1E2"
    };

    @FunctionalInterface
    interface ValueWrapperMethod {
        void perform(ValueWrapper vw, Object arg);

        // Helper method so we do not have to manually create a value wrapper and to simplify tests.
        default ValueWrapper performAndReturn(Object arg1, Object arg2) {
            ValueWrapper vw = new ValueWrapper(arg1);

            perform(vw, arg2);

            return vw;
        }
    }

    static void assertThrowsForInvalidTypes(ValueWrapperMethod method) {
        for (Object invalid1 : INVALID_TYPES) {
            ValueWrapper vw = new ValueWrapper(invalid1);

            // Test operations where both operands are invalid.
            for (Object invalid2 : INVALID_TYPES) {
                assertThrows(RuntimeException.class,
                        () -> method.perform(vw, invalid2));
            }

            for (Object valid : VALID_TYPES) {
                // Test operations where the right operand is invalid.
                assertThrows(RuntimeException.class,
                        () -> method.perform(vw, valid));

                // Test operations where the left operand is invalid.
                assertThrows(RuntimeException.class,
                        () -> method.perform(new ValueWrapper(valid), invalid1));
            }
        }
    }

    static void assertDoesNotThrowForValidTypes(ValueWrapperMethod method) {
        for (Object valid1 : VALID_TYPES) {
            ValueWrapper vw = new ValueWrapper(valid1);

            for (Object valid2 : VALID_TYPES) {
                assertDoesNotThrow(() -> method.perform(vw, valid2));
            }
        }
    }

    static void assertAssumesCorrectTypes(ValueWrapperMethod method, boolean zeroAllowed) {
        // Return value should be Double
        assertTrue(method.performAndReturn("3.3", 1).getValue() instanceof Double);
        assertTrue(method.performAndReturn(1, "3.3").getValue() instanceof Double);
        assertTrue(method.performAndReturn("1.3", "3.3").getValue() instanceof Double);

        assertTrue(method.performAndReturn(3.3, 1).getValue() instanceof Double);
        assertTrue(method.performAndReturn(1, 3.3).getValue() instanceof Double);
        assertTrue(method.performAndReturn(1.3, 3.3).getValue() instanceof Double);

        assertTrue(method.performAndReturn("3.3", null).getValue() instanceof Double);
        assertTrue(method.performAndReturn(null, "3.3").getValue() instanceof Double);

        // Return value should be Integer
        assertTrue(method.performAndReturn("33", 1).getValue() instanceof Integer);
        assertTrue(method.performAndReturn(1, "33").getValue() instanceof Integer);
        assertTrue(method.performAndReturn("13", "33").getValue() instanceof Integer);

        assertTrue(method.performAndReturn(33, 1).getValue() instanceof Integer);
        assertTrue(method.performAndReturn(1, 33).getValue() instanceof Integer);
        assertTrue(method.performAndReturn(13, 33).getValue() instanceof Integer);

        assertTrue(method.performAndReturn(null, "33").getValue() instanceof Integer);

        if (zeroAllowed) {
            assertTrue(method.performAndReturn("33", null).getValue() instanceof Integer);
            assertTrue(method.performAndReturn(null, null).getValue() instanceof Integer);
        }
    }

    @Test
    void testExamplesFromPDF() {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);

        v1.add(v2.getValue()); // v1 now stores Integer(0); v2 still stores null.
        assertEquals(0, v1.getValue());
        assertEquals(null, v2.getValue());

        ValueWrapper v3 = new ValueWrapper("1.2E1");
        ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));

        v3.add(v4.getValue()); // v3 now stores Double(13); v4 still stores Integer(1).
        assertEquals(13.0, v3.getValue());
        assertEquals(1, v4.getValue());

        ValueWrapper v5 = new ValueWrapper("12");
        ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));

        v5.add(v6.getValue()); // v5 now stores Integer(13); v6 still stores Integer(1).
        assertEquals(13, v5.getValue());
        assertEquals(1, v6.getValue());

        ValueWrapper v7 = new ValueWrapper("Ankica");
        ValueWrapper v8 = new ValueWrapper(Integer.valueOf(1));

        assertThrows(RuntimeException.class, () -> v7.add(v8.getValue())); // throws RuntimeException
    }

    @Test
    void testAddForInvalidTypes() {
        assertThrowsForInvalidTypes(ValueWrapper::add);
    }

    @Test
    void testAddForValidTypes() {
        assertDoesNotThrowForValidTypes(ValueWrapper::add);
    }

    @Test
    void testAddReturnTypes() {
        assertAssumesCorrectTypes(ValueWrapper::add, true);
    }

    @Test
    void testAddReturnValue() {
        ValueWrapperMethod method = ValueWrapper::add;

        assertEquals(1 + 2, method.performAndReturn("1" ,"2").getValue());
        assertEquals(1.0 + 2, method.performAndReturn("1.0" ,"2").getValue());
        assertEquals(1.0 + 2, method.performAndReturn("1" ,"2.0").getValue());

        assertEquals(100.0 + 2, method.performAndReturn("1E2" ,"2").getValue());

        assertEquals(0 + 2, method.performAndReturn(null, "2").getValue());
        assertEquals(0, method.performAndReturn(null, null).getValue());
        assertEquals(5 + 0, method.performAndReturn("5", null).getValue());
    }

    @Test
    void testSubtractForInvalidTypes() {
        assertThrowsForInvalidTypes(ValueWrapper::subtract);
    }

    @Test
    void testSubtractForValidTypes() {
        assertDoesNotThrowForValidTypes(ValueWrapper::subtract);
    }

    @Test
    void testSubtractReturnTypes() {
        assertAssumesCorrectTypes(ValueWrapper::subtract, true);
    }

    @Test
    void testSubtractReturnValue() {
        ValueWrapperMethod method = ValueWrapper::subtract;

        assertEquals(1 - 2, method.performAndReturn("1" ,"2").getValue());
        assertEquals(1.0 - 2, method.performAndReturn("1.0" ,"2").getValue());
        assertEquals(1.0 - 2, method.performAndReturn("1" ,"2.0").getValue());

        assertEquals(100.0 - 2, method.performAndReturn("1E2" ,"2").getValue());

        assertEquals(0 - 2, method.performAndReturn(null, "2").getValue());
        assertEquals(0, method.performAndReturn(null, null).getValue());
        assertEquals(5 - 0, method.performAndReturn("5", null).getValue());
    }

    @Test
    void testMultiplyForInvalidTypes() {
        assertThrowsForInvalidTypes(ValueWrapper::multiply);
    }

    @Test
    void testMultiplyForValidTypes() {
        assertDoesNotThrowForValidTypes(ValueWrapper::multiply);
    }

    @Test
    void testMultiplyReturnTypes() {
        assertAssumesCorrectTypes(ValueWrapper::multiply, true);
    }

    @Test
    void testMultiplyReturnValue() {
        ValueWrapperMethod method = ValueWrapper::multiply;

        assertEquals(1 * 2, method.performAndReturn("1" ,"2").getValue());
        assertEquals(1.0 * 2, method.performAndReturn("1.0" ,"2").getValue());
        assertEquals(1.0 * 2, method.performAndReturn("1" ,"2.0").getValue());

        assertEquals(100.0 * 2, method.performAndReturn("1E2" ,"2").getValue());

        assertEquals(0, method.performAndReturn(null, "2").getValue());
        assertEquals(0, method.performAndReturn(null, null).getValue());
        assertEquals(0, method.performAndReturn("5", null).getValue());
    }

    @Test
    void testDivideForInvalidTypes() {
        assertThrowsForInvalidTypes(ValueWrapper::divide);
    }

    @Test
    void testDivideForValidTypes() {
        assertDoesNotThrowForValidTypes(ValueWrapper::divide);
    }

    @Test
    void testDivideReturnTypes() {
        assertAssumesCorrectTypes(ValueWrapper::divide, false);
    }

    @Test
    void testDivideReturnValue() {
        ValueWrapperMethod method = ValueWrapper::divide;

        assertEquals(1 / 2, method.performAndReturn("1" ,"2").getValue());
        assertEquals(1.0 / 2, method.performAndReturn("1.0" ,"2").getValue());
        assertEquals(1.0 / 2, method.performAndReturn("1" ,"2.0").getValue());

        assertEquals(100.0 / 2, method.performAndReturn("1E2" ,"2").getValue());

        assertEquals(0 / 2, method.performAndReturn(null, "2").getValue());

        assertThrows(ArithmeticException.class, () -> method.performAndReturn(null, null));
        assertThrows(ArithmeticException.class, () -> method.performAndReturn("5", null));
    }

    @Test
    void testNumCompareForInvalidTypes() {
        assertThrowsForInvalidTypes(ValueWrapper::numCompare);
    }

    @Test
    void testNumCompareForValidTypes() {
        assertDoesNotThrowForValidTypes(ValueWrapper::numCompare);
    }

    @Test
    void testNumCompareReturnValue() {
        assertEquals(Integer.compare(0, 0), new ValueWrapper(null).numCompare(null));
        assertEquals(Integer.compare(1, 0), new ValueWrapper("1").numCompare(null));
        assertEquals(Integer.compare(0, 2), new ValueWrapper(null).numCompare("2"));

        assertEquals(Double.compare(1.2, 0), new ValueWrapper("1.2").numCompare(null));
        assertEquals(Double.compare(0, 2.1), new ValueWrapper(null).numCompare("2.1"));
    }

}