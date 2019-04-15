package hr.fer.zemris.java.hw06.crypto;

@FunctionalInterface
public interface ByteProcessor {

    void processBytes(byte[] bytes, int length);

}
