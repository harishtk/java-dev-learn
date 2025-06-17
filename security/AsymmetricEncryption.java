
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.HexFormat;

import javax.crypto.Cipher;

public class AsymmetricEncryption {
    public static void main(String[] args) {
        try {
            KeyPair keyPair = generateRSAKeyPair();

            // Input message
            String plainText = "Tonight we kill.";

            // Encrypt the message using the symmetric key
            byte[] cipherText = encrypt(plainText, keyPair.getPublic());
            
            System.out.println("The encrypted message is: " + cipherText);
            System.out.println("Raw bytes: " + new String(cipherText, StandardCharsets.UTF_8));
            System.out.println("Raw bytes: " + toBinaryString(cipherText).replace(' ', '0'));

            // Decrypt the message using the symmetric key
            String decryptedText = decrypt(cipherText, keyPair.getPrivate());

            System.out.println("The original message is: " + decryptedText);

            // Veriy digital signature
            byte[] digitalSignature = generateDigitalSignature(plainText.getBytes(), keyPair.getPrivate());
            System.out.println("Signature value: " + HexFormat.of().formatHex(digitalSignature));
            System.out.println("Verification: " + verify(plainText.getBytes(), digitalSignature, keyPair.getPublic()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateDigitalSignature(byte[] plainText, PrivateKey privateKey) 
        throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageHash = md.digest(plainText);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(messageHash);
    }

    public static boolean verify(byte[] plainText, byte[] digitalSignature, PublicKey publicKey) 
        throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedMessage = md.digest(plainText);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedMessageHash = cipher.doFinal(digitalSignature);

        return Arrays.equals(decryptedMessageHash, hashedMessage);
    }

    public static byte[] encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(byte[] cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText, StandardCharsets.UTF_8);
    }

    public static KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(3072);
        return keyPairGenerator.generateKeyPair();
    }

    public static String toBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)));
        }
        return sb.toString();
    }
}

