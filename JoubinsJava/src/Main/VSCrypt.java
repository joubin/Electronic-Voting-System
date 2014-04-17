package Main;
import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class VSCrypt {
    public VSCrypt() throws Exception {
        try {
            RSA_Generator();
        } catch ( Exception e ) {
            throw new Exception("Could not instantiate RSA Generator", e );
        }
    }
    public byte[] rsaEncrypt( byte[] data ) throws Exception {
        PublicKey pubkey;
        Cipher cipher;
        try {
            pubkey = readPubKeyFromFile("public.key");
            cipher = Cipher.getInstance("RSA");
        } catch ( Exception e ) {
            throw new IOException( "Cannot read key from public.key file", e );
        }
        cipher.init(Cipher.ENCRYPT_MODE, pubkey );
        byte[] cipherData = cipher.doFinal( data );
        System.out.println( "Returning CipherText: " + cipherData );
        return cipherData;
    }

    public byte[] rsaDecrypt( byte[] data ) throws Exception {
        PrivateKey privkey;
        Cipher cipher;
        try {
            privkey = readPrivKeyFromFile("private.key");
            cipher = Cipher.getInstance("RSA");
        } catch ( Exception e ) {
            throw new IOException("Cannot read key from private.key", e );
        }
        cipher.init(Cipher.DECRYPT_MODE, privkey );
        byte[] plainData = cipher.doFinal( data );
        System.out.println( "Returning PlainText: " + plainData );
        return plainData;
    }

    private PrivateKey readPrivKeyFromFile( String keyFileName ) throws IOException {
        InputStream in = new FileInputStream( keyFileName );
        ObjectInputStream oin = new ObjectInputStream( new BufferedInputStream( in ));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec( m, e );
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey privkey = fact.generatePrivate( keySpec );
            System.out.println( "returning privkey: " + privkey );
            return privkey;
        } catch ( Exception e ) {
            throw new RuntimeException( "Spurious serialization error", e );
        } finally {
            oin.close();
        }
    }

    private PublicKey readPubKeyFromFile( String keyFileName ) throws IOException {
        InputStream in = new FileInputStream( keyFileName );
        ObjectInputStream oin = new ObjectInputStream( new BufferedInputStream( in ));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec( m, e );
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubkey = fact.generatePublic( keySpec );
            System.out.println( "returning pubkey: " + pubkey );
            return pubkey;
        } catch ( Exception e ) {
            throw new RuntimeException( "Spurious serialization error", e );
        } finally {
            oin.close();
        }
    }

    private void RSA_Generator() throws Exception {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.genKeyPair();
            Key publicKey = kp.getPublic();
            System.out.println( "public key: " + publicKey );
            Key privateKey = kp.getPrivate();
            System.out.println( "private key: " + privateKey );
            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = fact.getKeySpec( publicKey, RSAPublicKeySpec.class);
            RSAPrivateKeySpec priv = fact.getKeySpec( privateKey, RSAPrivateKeySpec.class);
            saveToFile( "public.key", pub.getModulus(), pub.getPublicExponent());
            saveToFile( "private.key", priv.getModulus(), priv.getPrivateExponent());
            System.out.println( "Exiting RSA_Generator" );
        } catch ( Exception e ) {
            throw new IOException("Cannot generate keypair", e );
        }
    }

    private void saveToFile( String filename, BigInteger mod, BigInteger exp ) throws IOException {
        ObjectOutputStream oout = new ObjectOutputStream( new BufferedOutputStream( new FileOutputStream( filename )));
        try {
            oout.writeObject( mod );
            oout.writeObject( exp );
        } catch ( Exception e ) {
            throw new IOException( "Unexpected error", e );
        } finally {
            oout.close();
        }
    }


    public static void main( String[] args ) throws Exception {
        System.out.println("Starting VSCrypt");
        try {
            VSCrypt x = new VSCrypt();
            String passphrase = new String( "ThisIsMyPassphrase" );
            System.out.println( passphrase );
            byte[] pb = passphrase.getBytes( "UTF8" );
            System.out.println( "Passphrase as byte array: " + pb );
            byte[] encryptedRSA = x.rsaEncrypt( pb );
            System.out.println( "Encryption: " + encryptedRSA );
            byte[] decryptedRSA = x.rsaDecrypt( encryptedRSA );
            System.out.println( "Decryption: " + decryptedRSA );
            System.out.println( "Finished" );
        } catch ( Exception e ) {
            throw new Exception("Could not make VSCrypt", e );
        }
    }
}