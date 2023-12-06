import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalBob
{
	private static boolean verifySignature(	BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message)
	{
		// IMPLEMENT THIS FUNCTION;
		BigInteger m = new BigInteger(message.getBytes());

		// Calculate v1 = g^m mod p
		BigInteger v1 = g.modPow(m, p);

		// Calculate v2 = y^a * a^b mod p
		BigInteger v2 = y.modPow(a, p).multiply(a.modPow(b, p)).mod(p);

		// If v1 equals v2, then the signature is valid
		return v1.equals(v2);
	}

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());

		// read public key
		BigInteger y = (BigInteger)is.readObject();
		BigInteger g = (BigInteger)is.readObject();
		BigInteger p = (BigInteger)is.readObject();

		// read message
		String message = (String)is.readObject();

		// read signature
		BigInteger a = (BigInteger)is.readObject();
		BigInteger b = (BigInteger)is.readObject();

		boolean result = verifySignature(y, g, p, a, b, message);

		System.out.println(message);

		if (result == true)
			System.out.println("Signature verified.");
		else
			System.out.println("Signature verification failed.");

		s.close();
	}
}