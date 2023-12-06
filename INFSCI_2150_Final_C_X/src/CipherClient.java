import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.security.*;
import javax.crypto.*;

public class CipherClient
{
	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		// String host = "paradox.sis.pitt.edu";
		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		// YOU NEED TO DO THESE STEPS:
		SecretKey desKey;
		// Read the DES key from the file if the file already exists.
		File fl = new File("deskey.file");
		if (fl.exists()){
			try (ObjectInputStream keyFile = new ObjectInputStream(new FileInputStream("deskey.file"))) {
				desKey = (SecretKey) keyFile.readObject();
			}
		}else{
			// Otherwise generate a new DES key and store it in the new file.
			// -Generate a DES key.
			KeyGenerator keyGen = KeyGenerator.getInstance("DES");
			desKey = keyGen.generateKey();
			// -Store it in a file.
			try (ObjectOutputStream keyFile = new ObjectOutputStream(new FileOutputStream("deskey.file"))) {
				keyFile.writeObject(desKey);
				keyFile.flush();
			}
		}
		// -Use the key to encrypt the message above and send it over socket s to the server.
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, desKey);
		byte[] encryptedMessage = cipher.doFinal(message.getBytes());

		os.writeObject(encryptedMessage);

		s.close();
	}
}