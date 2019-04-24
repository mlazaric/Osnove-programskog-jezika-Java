package hr.fer.zemris.java.hw06.crypto;

import org.junit.jupiter.api.Test;

import static hr.fer.zemris.java.hw06.crypto.Util.bytetohex;
import static hr.fer.zemris.java.hw06.crypto.Util.hextobyte;
import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void testHexToByte() {
        assertArrayEquals(new byte[] {1, -82, 34}, hextobyte("01aE22"));
    }

    @Test
    void testByteToHex() {
        assertEquals("01ae22",  bytetohex(new byte[] {1, -82, 34}));
    }

    @Test
    void testHexToByteWithOddLength() {
        assertThrows(IllegalArgumentException.class, () -> hextobyte("123"));
    }

    @Test
    void testHexToByteWithInvalidCharacter() {
        assertThrows(IllegalArgumentException.class, () -> hextobyte("1G"));
        assertThrows(IllegalArgumentException.class, () -> hextobyte("1^"));
        assertThrows(IllegalArgumentException.class, () -> hextobyte("@%"));
    }

    @Test
    void testHexToByteWithZeroLength() {
        assertArrayEquals(new byte[] {}, hextobyte(""));
    }

}