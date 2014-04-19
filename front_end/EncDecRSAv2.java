
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.security.Security;

public class EncDecRSAv2 {
    public static byte[] pemToDer(String pemKey) throws GeneralSecurityException {
        String[] parts = pemKey.split("-----");
        return DatatypeConverter.parseBase64Binary(parts[parts.length / 2]);
    }

    public static PublicKey derToPublicKey(byte[] asn1key) throws GeneralSecurityException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(asn1key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        return keyFactory.generatePublic(spec);
    }

    public static byte[] encrypt(PublicKey publicKey, String text) throws GeneralSecurityException {
        Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "BC");
        rsa.init(Cipher.ENCRYPT_MODE, publicKey);
        return rsa.doFinal(text.getBytes());
    }

    static String readFile(String path)
            throws IOException
    {
        String line = null;
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            StringBuilder sb = new StringBuilder();
            line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();

        }

    }
    public static void main(String[] args) throws IOException, GeneralSecurityException {
				Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        String publicKey = readFile("key.public");
        byte[] pem = pemToDer(publicKey);
        PublicKey myKey = derToPublicKey(pem);
        String sendMessage = "{'vid_hash': '917ef7e7be4a84e279b74a257953307f1cff4a2e3d221e363ead528c6b556edb', 'state': 'ballot_response', 'userInfo': {'ssn': '700-33-6870', 'pin': '1234', 'vid': '265jMeges'}}";
        byte[] encryptedDATA = encrypt(myKey, sendMessage);
        System.out.println(encryptedDATA.toString());
        Socket smtpSocket = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        try {
            smtpSocket = new Socket("172.16.186.133", 9999);
            os = new DataOutputStream(smtpSocket.getOutputStream());
            is = new DataInputStream(smtpSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: hostname");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: hostname");
        }

        if (smtpSocket != null && os != null && is != null) {
            try {
                System.out.println("sending message");
                os.writeBytes(encryptedDATA+"\n");
                os.close();
                is.close();
                smtpSocket.close();
            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }
}
