import javax.crypto.Cipher;
import java.io.*;
import java.net.*;
import java.security.*;

public class RSABob {
    public static void main(String[] args) throws Exception {

        // Generate Bob's RSA key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        sendPublicKeyToAlice(publicKey);

        int port = 7999;
        try (ServerSocket server = new ServerSocket(port);
             Socket s = server.accept();
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            // Receiving the message and signature from Alice
            byte[] encryptedMessage = (byte[]) is.readObject();
            byte[] signature = (byte[]) is.readObject();
            PublicKey alicePublicKey = (PublicKey) is.readObject();

            // decrypt the message by Bob's private key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedMessageBytes = cipher.doFinal(encryptedMessage);
            String message = new String(decryptedMessageBytes);

            // Verifying the signature
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(alicePublicKey);
            sign.update(message.getBytes());

            boolean isVerified = sign.verify(signature);
            System.out.println("Signature verified: " + isVerified);
            System.out.println("Received Message: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void sendPublicKeyToAlice(PublicKey publicKey) throws Exception{
        // "Sending" the Public key to Alice
        String host = "127.0.0.1";
        int portAlie = 8000;
        try (Socket s = new Socket(host, portAlie);
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream())) {
            os.writeObject(publicKey);
        }
    }
}
