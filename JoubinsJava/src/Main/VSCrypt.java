package Main;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;


public class VSCrypt {

//    private static byte[] sharedKey = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16};

//    public VSCrypt() throws Exception {
//        try {
//            RSA_Generator();
//        } catch ( Exception e ) {
//            throw new Exception("Could not instantiate RSA Generator", e );
//        }
//    }

    public  String encrypt(byte[] key,String strToEncrypt) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
            return encryptedString;
        } catch (Exception e) {
            throw new Exception("Error in AES Encryption", e);
        }
    }

    public  String decrypt(byte[] key, String strToDecrypt) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
            return decryptedString;
        } catch (Exception e) {
            throw new Exception("Error in AES Decryption", e);
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

    private static void saveToFile( String filename, BigInteger mod, BigInteger exp ) throws IOException {
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

    public  byte[] sha256digest(String[] list) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        for(String s: list){
            digest.update(s.getBytes());
        }
        byte[]  b = digest.digest();
        char[] s = Hex.encodeHex(new String(b).getBytes());
        return new String(s).getBytes("UTF-8");
//        return s;
    }
//    public static void main( String[] args ) throws Exception {
//        try {
//            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//            kpg.initialize(2048);
//            KeyPair kp = kpg.genKeyPair();
//            Key publicKey = kp.getPublic();
//            System.out.println( "public key: " + publicKey );
//            Key privateKey = kp.getPrivate();
//            System.out.println( "private key: " + privateKey );
//            KeyFactory fact = KeyFactory.getInstance("RSA");
//            RSAPublicKeySpec pub = fact.getKeySpec( publicKey, RSAPublicKeySpec.class);
//            RSAPrivateKeySpec priv = fact.getKeySpec( privateKey, RSAPrivateKeySpec.class);
//            saveToFile( "public.key", pub.getModulus(), pub.getPublicExponent());
//            saveToFile( "private.key", priv.getModulus(), priv.getPrivateExponent());
//            System.out.println( "Exiting RSA_Generator" );
//        } catch ( Exception e ) {
//            throw new IOException("Cannot generate keypair", e );
//        }
//        System.out.println("Starting VSCrypt");
//        try {
//            VSCrypt x = new VSCrypt();
//            String passphrase = new String( "ThisIsMyPassphrase" );
//            System.out.println( passphrase );
//            byte[] pb = passphrase.getBytes( "UTF8" );
//            System.out.println( "Passphrase as byte array: " + pb );
//            byte[] encryptedRSA = x.rsaEncrypt( pb );
//            System.out.println( "Encryption: " + encryptedRSA );
//            byte[] decryptedRSA = x.rsaDecrypt( encryptedRSA );
//            System.out.println( "Decryption: " + decryptedRSA );
//            System.out.println( "Finished" );
//        } catch ( Exception e ) {
//            throw new Exception("Could not make VSCrypt", e );
//        }
//    }
}