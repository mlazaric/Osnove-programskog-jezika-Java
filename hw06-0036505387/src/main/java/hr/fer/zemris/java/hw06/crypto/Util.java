package hr.fer.zemris.java.hw06.crypto;

public final class Util {

    private static final int HEX_DIGIT_MASK = 0xF;
    private static final int HEX_RADIX = 16;
    private static final int HEX_NUMBER_OF_BITS = 4;

    private Util() {}

    public static byte[] hextobyte(String keyText) {
        if ((keyText.length() % 2) != 0) {
            // If its length is odd, it does not represent a valid hex string
            throw new IllegalArgumentException("keyText cannot have odd length.");
        }

        // Each byte is represented by two hex digits
        byte[] bytes = new byte[keyText.length() / 2];

        for (int index = 0; index < bytes.length; index++) {
            bytes[index] = parseSingleByte(keyText.charAt(2 * index), keyText.charAt(2 * index + 1));
        }

        return bytes;
    }

    private static byte parseSingleByte(char c1, char c2) {
        int digit1 = Character.digit(c1, HEX_RADIX);
        int digit2 = Character.digit(c2, HEX_RADIX);

        if (digit1 == -1 || digit2 == -1) {
            throw new IllegalArgumentException("'" + c1 + c2 + "' is not a valid byte.");
        }

        // digit1 represents the first 4 bits of the byte, so it needs to be shifted 4 bits to the left
        // digit2 represents the last 4 bits of the byte
        return (byte) ((digit1 << HEX_NUMBER_OF_BITS) + digit2);
    }

    public static String bytetohex(byte[] bytearray) {
        StringBuilder sb = new StringBuilder(bytearray.length * 2);

        for (byte currentByte : bytearray) {
            // Get the first 4 bits (first hex digit)
            sb.append(Character.forDigit((currentByte >> HEX_NUMBER_OF_BITS) & HEX_DIGIT_MASK, HEX_RADIX));

            // Get the last 4 bits (second hex digit)
            sb.append(Character.forDigit(currentByte & HEX_DIGIT_MASK, HEX_RADIX));
        }

        return sb.toString();
    }

}
