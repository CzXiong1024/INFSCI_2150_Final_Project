import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

public class CipherServer
{
	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		ObjectInputStream is = new ObjectInputStream(s.getInputStream());

		// YOU NEED TO DO THESE STEPS:
		// -Read the key from the file generated by the client.
		SecretKey desKey;
		try (ObjectInputStream keyFile = new ObjectInputStream(new FileInputStream("deskey.file"))) {
			desKey = (SecretKey) keyFile.readObject();
		}
		// -Use the key to decrypt the incoming message from socket s.
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, desKey);
		byte[] encryptedMessage = (byte[]) is.readObject();
		String decryptedMessage = new String(cipher.doFinal(encryptedMessage));
		// -Print out the decrypt String to see if it matches the original message.
		System.out.println("Decrypted message: " + decryptedMessage);

		s.close();
		server.close();
	}
}