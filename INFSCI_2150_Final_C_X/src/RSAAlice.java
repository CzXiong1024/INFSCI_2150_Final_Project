import javax.crypto.Cipher;
import java.io.*;
import java.net.*;
import java.security.*;

public class RSAAlice {
    public static void main(String[] args) throws Exception {
        // Generate RSA key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        PublicKey bobPublicKey = getBobsPublicKey();
        // Message to be signed
        String message = "Hello, Bob! This is Alice talking to you!";

        // Signing the message
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(message.getBytes());
        byte[] signature = sign.sign();

        // Encrypt the message
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, bobPublicKey);
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());

        // "Sending" the encrypted message and signature to Bob
        String host = "127.0.0.1";
        int port = 7999;
        try (Socket s = new Socket(host, port);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream())) {
            os.writeObject(encryptedMessage);
            os.writeObject(signature);
            os.writeObject(publicKey);
        }
    }

    // Get Bob's public key
    public static PublicKey getBobsPublicKey(){
        int port = 8000;
        PublicKey BobPublicKey = null;
        try (ServerSocket server = new ServerSocket(port);
             Socket s = server.accept();
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            // Receiving Bob's public key
            BobPublicKey = (PublicKey) is.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return BobPublicKey;
    }
}
