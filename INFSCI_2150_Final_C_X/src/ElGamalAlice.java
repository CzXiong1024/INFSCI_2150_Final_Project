import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalAlice
{
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d)
	{
		// IMPLEMENT THIS FUNCTION;
		// Y = g^d mod p
		return g.modPow(d, p);
	}

	private static BigInteger computeK(BigInteger p)
	{
		// IMPLEMENT THIS FUNCTION;
		// K should be a random number that is relatively prime to (p-1), which means gcd(K, p-1) = 1
		BigInteger k;
		SecureRandom sRandom = new SecureRandom();
		do {
			k = new BigInteger(p.bitLength() - 1, sRandom);
		} while (!k.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE));
		return k;
	}
	
	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k)
	{
		// IMPLEMENT THIS FUNCTION;
		// A = g^k mod p
		return g.modPow(k, p);
	}

	private static BigInteger computeB(	String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p)
	{
		// IMPLEMENT THIS FUNCTION;
		// B = (message - d * A) * k^(-1) mod (p-1)
		BigInteger m = new BigInteger(message.getBytes());
		BigInteger inverseK = k.modInverse(p.subtract(BigInteger.ONE));
		return m.subtract(d.multiply(a)).multiply(inverseK).mod(p.subtract(BigInteger.ONE));
	}

	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";

		// String host = "paradox.sis.pitt.edu";
		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find out what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int mStrength = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

		// Create a BigInterger with mStrength bit length that is highly likely to be prime.
		// (The '16' determines the probability that p is prime. Refer to BigInteger documentation.)
		p = new BigInteger(mStrength, 16, mSecureRandom);
		
		// Create a randomly generated BigInteger of length mStrength-1
		g = new BigInteger(mStrength-1, mSecureRandom);
		d = new BigInteger(mStrength-1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now compute the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);
		
		// send signature
		os.writeObject(a);
		os.writeObject(b);
		
		s.close();
	}
}