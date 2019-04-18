package hr.fer.zemris.java.hw06.crypto;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static hr.fer.zemris.java.hw06.crypto.Crypto.calculateSHA;
import static org.junit.jupiter.api.Assertions.*;

class CryptoTest {

    @Test
    @Disabled // Disabled since the file is not included in the zip.
    void testCalculateSHAWith_hw06testbin() {
        assertEquals("2e7b3a91235ad72cb7e7f6a721f077faacfeafdea8f3785627a5245bea112598", calculateSHA("hw06test.bin"));
    }

}