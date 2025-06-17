
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.HexFormat;

public class DigitalSignatureSample {
    public static final String Ed25519 = "Ed25519";

    public static void main(String[] args) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Ed25519);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            String plainText = "Untampered message! Hurray!!";

            byte[] digitalSignature = generateDigitalSignature(plainText.getBytes(), keyPair.getPrivate());

            System.out.println("Signature value: " + HexFormat.of().formatHex(digitalSignature));
            System.out.println("Verification: " + verifyDigitalSignature(plainText.getBytes(), digitalSignature, keyPair.getPublic()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyDigitalSignature(byte[] plainText, byte[] digitalSignature, PublicKey publicKey)
        throws Exception {
        Signature signature = Signature.getInstance(Ed25519);
        signature.initVerify(publicKey);
        signature.update(plainText);
        return signature.verify(digitalSignature);
    }

    public static byte[] generateDigitalSignature(byte[] plainText, PrivateKey privateKey) 
        throws Exception {
        Signature signature = Signature.getInstance(Ed25519);
        signature.initSign(privateKey);
        signature.update(plainText);
        return signature.sign();
    }
}
