package hr.fer.zemris.java.hw06.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Scanner;

import static hr.fer.zemris.java.hw06.crypto.Util.bytetohex;
import static hr.fer.zemris.java.hw06.crypto.Util.hextobyte;

public class Crypto {

    public static void main(String[] args){
        if (args.length == 0) {
            System.out.println("Requires at least one argument specifying which operation to perform.");
            System.out.println("Valid operations are: checksha, encrypt and decrypt.");
            System.exit(1);
        }

        try (Scanner sc = new Scanner(System.in)) {
            String[] commandArguments = Arrays.copyOfRange(args, 1, args.length);

            switch (args[0]) {
                case "checksha":
                    handleCheckSha(commandArguments, sc);
                    break;

                case "encrypt":
                    handleCrypt(commandArguments, sc, true);
                    break;

                case "decrypt":
                    handleCrypt(commandArguments, sc,  false);
                    break;

                default:
                    System.out.println("'" + args[0] + "' is not a valid operation.");
                    System.exit(1);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void processFile(String filename, ByteProcessor processor) {
        byte[] buffer = new byte[1024];

        try (InputStream inputStream = Files.newInputStream(Paths.get(filename))) {
            while (true) {
                int length = inputStream.read(buffer);

                if (length == -1) {
                    break;
                }

                processor.processBytes(buffer, length);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading from file '" + filename + "'.");
        }
    }

    private static Cipher getCipherFromUserInput(String keyText, String ivText, boolean encrypt) {
        SecretKeySpec keySpec = new SecretKeySpec(hextobyte(keyText), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(hextobyte(ivText));
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Invalid algorithm used for encryption/decryption for Cipher.");
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("Invalid padding used for Cipher.");
        }

        try {
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key used for Cipher initialisation.");
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Invalid algorithm parameter used for Cipher initialisation.");
        }

        return cipher;
    }

    private static void handleCheckSha(String[] arguments, Scanner sc) {
        if (arguments.length != 1) {
            throw new IllegalArgumentException("checksha expects 1 argument (file name), " + arguments.length + " were given.");
        }

        String filename = arguments[0];

        System.out.println("Please provide expected sha-256 digest for " + filename + ":");
        System.out.print("> ");

        String expectedDigest = sc.next();
        String actualDigest = calculateSHA(filename);

        System.out.print("Digesting completed. ");

        if (expectedDigest.equals(actualDigest)) {
            System.out.println("Digest of " + filename + " matches expected digest.");
        }
        else {
            System.out.println("Digest of " + filename + " does not match the expected digest. Digest was: " + actualDigest);
        }
    }

    static String calculateSHA(String filename) { // Package private so it can be tested
        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) { // Wrap checked exception and add a relevant message
            throw new RuntimeException("Invalid algorithm used for calculating message digest.");
        }

        processFile(filename, (bytes, length) -> digest.update(bytes, 0, length));

        return bytetohex(digest.digest());
    }

    private static void handleCrypt(String[] arguments, Scanner sc, boolean encrypt) {
        String operationVerb = encrypt ? "encrypt" : "decrypt";

        if (arguments.length != 2) {
            throw new IllegalArgumentException(operationVerb + " expects 2 arguments (input file and output file), " + arguments.length + " were given.");
        }

        String inputFile = arguments[0];
        String outputFile = arguments[1];

        System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
        System.out.print("> ");
        String password = sc.next();
        System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
        System.out.print("> ");
        String initVector = sc.next();

        try (OutputStream outputStream = Files.newOutputStream(Paths.get(outputFile))) {
            Cipher cipher = getCipherFromUserInput(password, initVector, encrypt);

            processFile(inputFile, (bytes, length) -> {
                try {
                    outputStream.write(cipher.update(bytes, 0, length));
                } catch (IOException e) {
                    throw new RuntimeException("Error occurred while writing to file '" + outputFile + "'.");
                }
            });

            outputStream.write(cipher.doFinal());
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while writing to file '" + outputFile + "'.");
        } catch (BadPaddingException e) {
            throw new RuntimeException("Cannot " + operationVerb + " due to bad padding.");
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("Cannot " + operationVerb + " due to an illegal block size.");
        }

        String operationName = encrypt ? "Encryption" : "Decryption";

        System.out.println(operationName + " completed. Generated file " + outputFile + " based on file " + inputFile + ".");

    }

}
