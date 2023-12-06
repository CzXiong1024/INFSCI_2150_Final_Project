import java.io.*;
import java.security.*;

public class Protection
{
	public static byte[] makeBytes(long t, double q) 
	{    
		try 
		{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			DataOutputStream dataOut = new DataOutputStream(byteOut);
			dataOut.writeLong(t);
			dataOut.writeDouble(q);
			return byteOut.toByteArray();
		}
		catch (IOException e) 
		{
			return new byte[0];
		}
	}	

	public static byte[] makeDigest(byte[] mush, long t2, double q2) throws NoSuchAlgorithmException 
	{
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(mush);
		md.update(makeBytes(t2, q2));
		return md.digest();
	}
	

	public static byte[] makeDigest(String user, String password,
									long t1, double q1)
									throws NoSuchAlgorithmException
	{
		// IMPLEMENT THIS FUNCTION.
		MessageDigest md = MessageDigest.getInstance("SHA");

		// Update the digest using the username and password
		md.update(user.getBytes());
		md.update(password.getBytes());

		// Update the digest using the timestamp and random number
		md.update((makeBytes(t1,q1)));

		return md.digest();
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = String.format("%02x", b);
			hexString.append(hex);
		}
		return hexString.toString();
	}

}