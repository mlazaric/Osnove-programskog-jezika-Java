package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectMultistackTest {

    static ObjectMultistack createObjectMultistackWithNEntries(int n, String key) {
        ObjectMultistack multistack = new ObjectMultistack();

        for (int index = 0; index < n; ++index) {
            multistack.push(key, new ValueWrapper(index));
        }

        return multistack;
    }

    @Test
    void testAddingNullKey() {
        assertThrows(NullPointerException.class,
                () -> new ObjectMultistack().push(null, new ValueWrapper("a")));
    }

    @Test
    void testAddingNullValue() {
        assertThrows(NullPointerException.class,
                () -> new ObjectMultistack().push("string", null));
    }

    @Test
    void testIsEmptyOfEmptyEntry() {
        assertTrue(createObjectMultistackWithNEntries(0, "key").isEmpty("key"));
    }

    @Test
    void testIsEmptyOfNonEmptyEntry() {
        assertFalse(createObjectMultistackWithNEntries(5, "key").isEmpty("key"));
    }

    @Test
    void testPushingToEmptyEntry() {
        ObjectMultistack multistack = new ObjectMultistack();

        multistack.push("key", new ValueWrapper("value"));

        assertFalse(multistack.isEmpty("key"));
        assertEquals(new ValueWrapper("value"), multistack.peek("key"));
        assertEquals(new ValueWrapper("value"), multistack.pop("key"));
        assertTrue(multistack.isEmpty("key"));
    }

    @Test
    void testPushingToNonEmptyEntry() {
        ObjectMultistack multistack = createObjectMultistackWithNEntries(5, "key");

        multistack.push("key", new ValueWrapper("value"));

        assertFalse(multistack.isEmpty("key"));
        assertEquals(new ValueWrapper("value"), multistack.peek("key"));
        assertEquals(new ValueWrapper("value"), multistack.pop("key"));
        assertFalse(multistack.isEmpty("key"));
    }

    @Test
    void testPoppingOfEmptyEntry() {
        assertThrows(EmptyMultistackEntryException.class,
                () -> new ObjectMultistack().pop("key"));
    }

    @Test
    void testPoppingOfNonEmptyEntry() {
        ObjectMultistack multistack = createObjectMultistackWithNEntries(5, "key");

        for (int index = 4; index >= 0; --index) {
            assertEquals(new ValueWrapper(index), multistack.pop("key"));
        }

        assertTrue(multistack.isEmpty("key"));
    }

    @Test
    void testPeekingEmptyEntry() {
        assertThrows(EmptyMultistackEntryException.class,
                () -> new ObjectMultistack().peek("key"));
    }

    @Test
    void testPeekingNonEmptyEntry() {
        ObjectMultistack multistack = createObjectMultistackWithNEntries(5, "key");

        for (int index = 4; index >= 0; --index) {
            assertEquals(new ValueWrapper(4), multistack.peek("key"));
        }

        assertFalse(multistack.isEmpty("key"));
    }
}