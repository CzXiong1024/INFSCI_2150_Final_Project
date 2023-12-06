import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;
import java.util.Random;

public class ProtectedClient
{
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException 
	{
		DataOutputStream out = new DataOutputStream(outStream);

		// IMPLEMENT THIS FUNCTION.
		long t1 = (new Date()).getTime();
		double q1 = (new Random()).nextDouble();

		// Generate the first digest
		byte[] digest1 = Protection.makeDigest(user,password,t1,q1);
		System.out.println(Protection.bytesToHex(digest1));
		long t2 = (new Date()).getTime();
		double q2 = (new Random()).nextDouble();

		// Generate the second digest
		byte[] digest2 = Protection.makeDigest(digest1,t2,q2);
		System.out.println(Protection.bytesToHex(digest2));
		// Send the related data to the server
		out.writeUTF(user);
		out.writeLong(t1);
		out.writeDouble(q1);
		out.writeInt(digest1.length);
		out.write(digest1);

		out.writeLong(t2);
		out.writeDouble(q2);
		out.writeInt(digest2.length);
		out.write(digest2);
		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
		// String host = "paradox.sis.pitt.edu";
		// For test, the host was replaced by the ip for current machine 127.0.0.1
		String host = "127.0.0.1";
		int port = 7999;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}