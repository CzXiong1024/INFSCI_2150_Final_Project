import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Arrays;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{
		DataInputStream in = new DataInputStream(inStream);

		// IMPLEMENT THIS FUNCTION.
		String user = in.readUTF();
		// System.out.println(user);
		long t1 = in.readLong();
		double q1 = in.readDouble();
		int d1len = in.readInt();
		byte[] digest1 = new byte[d1len];
		in.readFully(digest1);
		System.out.println("Server Received digest1: "+ Protection.bytesToHex(digest1));

		long t2 = in.readLong();
		double q2 = in.readDouble();
		int d2len = in.readInt();
		byte[] digest2 = new byte[d2len];
		in.readFully(digest2);
		System.out.println("Server Received digest2: "+ Protection.bytesToHex(digest2));
		// Retrieve the password from the database
		String password = lookupPassword(user);
		// get the SHA hash values using the same methods from Protection
		byte[] check1 = Protection.makeDigest(user, password, t1, q1);
		byte[] check2 = Protection.makeDigest(check1, t2, q2);
		System.out.println("Server newly Generated digest1: "+ Protection.bytesToHex(check1));
		System.out.println("Server newly Generated digest2: "+ Protection.bytesToHex(check2));
		return Arrays.equals(check1, digest1) && Arrays.equals(check2, digest2);
	}

	protected String lookupPassword(String user) {
		return "abc123";
	}

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		System.out.println("Waiting for user log in...");
		Socket client = s.accept();
		ProtectedServer server = new ProtectedServer();
		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");
		s.close();
	}
}