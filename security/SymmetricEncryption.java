import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class SymmetricEncryption {
    public static void main(String[] args) {
        try {
            SecretKey symSecretKey = generateKey();
            IvParameterSpec iv = generateIv();

            String plainText = "Tonight we kill";

            // Encrypt the message using the symmetric key
            byte[] cipherText = encrypt(plainText, symSecretKey, iv);
            
            System.out.println("The encrypted message is: " + cipherText);
            System.out.println("Raw bytes: " + new String(cipherText, StandardCharsets.UTF_8));
            System.out.println("Raw bytes: " + toBinaryString(cipherText).replace(' ', '0'));

            // Decrypt the message using the symmetric key
            String decryptedText = decrypt(cipherText, symSecretKey, iv);

            System.out.println("The original message is: " + decryptedText);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(String input, SecretKey key, IvParameterSpec iv) 
        throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(byte[] cipherText, SecretKey key, IvParameterSpec iv) 
        throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText);
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    } 
    
    public static IvParameterSpec generateIv() {
        byte[] initializationVector = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initializationVector);
        return new IvParameterSpec(initializationVector);
    }

    public static String toBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)));
        }
        return sb.toString();
    }
}
