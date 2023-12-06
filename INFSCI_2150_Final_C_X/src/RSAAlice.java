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

        // Message to be signed
        String message = "Hello, Bob! This is Alice talking to you!";

        // Signing the message
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(message.getBytes());
        byte[] signature = sign.sign();

        // "Sending" the message and signature to Bob
        String host = "127.0.0.1";
        int port = 7999;
        try (Socket s = new Socket(host, port);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream())) {
            os.writeObject(message);
            os.writeObject(signature);
            os.writeObject(publicKey);
        }
    }
}
