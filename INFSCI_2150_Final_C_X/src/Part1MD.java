import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Part1MD {

    public static void main(String[] args) {
        // Scanner to read user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the string to hash:");
        String input = scanner.nextLine();

        // Hash with MD5
        String md5Hashed = hashString(input, "MD5");
        System.out.println("MD5 Hash: " + md5Hashed);

        // Hash with SHA
        String shaHashed = hashString(input, "SHA");
        System.out.println("SHA Hash: " + shaHashed);
    }

    private static String hashString(String input, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(input.getBytes());
            return byteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception here
            return "Error: Algorithm not found.";
        }
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
