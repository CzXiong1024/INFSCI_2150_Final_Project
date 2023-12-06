import java.io.*;
import java.net.*;
import java.security.*;

public class RSABob {
    public static void main(String[] args) {
        int port = 7999;
        try (ServerSocket server = new ServerSocket(port);
             Socket s = server.accept();
             ObjectInputStream is = new ObjectInputStream(s.getInputStream())) {

            // Receiving the message and signature from Alice
            String message = (String) is.readObject();
            byte[] signature = (byte[]) is.readObject();
            PublicKey publicKey = (PublicKey) is.readObject();

            // Verifying the signature
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(publicKey);
            sign.update(message.getBytes());

            boolean isVerified = sign.verify(signature);
            System.out.println("Signature verified: " + isVerified);
            System.out.println("Received Message: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
