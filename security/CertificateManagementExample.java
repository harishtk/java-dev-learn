
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HexFormat;

public class CertificateManagementExample {
    public static void main(String[] args) {
        try {
            X509Certificate cert = extractCertificate("security/mycert.pem");

            // compute thumprint
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.digest(cert.getEncoded());
            System.out.print("SHA-256: ");
            System.out.println(HexFormat.ofDelimiter(":").formatHex(md.digest()));

            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            md.digest(cert.getEncoded());
            System.out.print("SHA-1: ");
            System.out.println(HexFormat.ofDelimiter(":").formatHex(sha1.digest()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static X509Certificate extractCertificate(String filePath) 
        throws IOException, CertificateException {
        try (InputStream in = Files.newInputStream(Path.of(filePath).toAbsolutePath(), StandardOpenOption.READ)) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certificateFactory.generateCertificate(in);
        }
    }
}
