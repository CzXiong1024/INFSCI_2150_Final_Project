import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;

public class X509Client {
    public static void main(String[] args) throws Exception {
        //  Set the truststore system properties
        System.setProperty("javax.net.ssl.trustStore", "src/X509Keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");

        String host = "127.0.0.1";
        int port = 7999;
        SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) ssf.createSocket(host, port);

        // Load server's certificate from .cer file (consider it is sent from server)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream fis = new FileInputStream("src/CXServerCert.cer");
        X509Certificate serverCertFromFile = (X509Certificate) cf.generateCertificate(fis);

        // Get server's certificate from the current session
        SSLSession session = socket.getSession();
        Certificate[] serverCerts = session.getPeerCertificates();
        X509Certificate serverCert = (X509Certificate) serverCerts[0];

        // Verify the certificate manually
        // Compare the two certificates
        if (serverCertFromFile.equals(serverCert)) {
            System.out.println("Server's certificate verified successfully.");
        } else {
            System.out.println("Server's certificate verification failed!");
        }

        PublicKey serverPublicKey = serverCert.getPublicKey(); // Extract public key

        // Print the certificate
        System.out.println("Server Certificate Details:");
        System.out.println("Issuer: " + serverCert.getIssuerDN());
        System.out.println("Serial Number: " + serverCert.getSerialNumber());
        System.out.println("Valid from: " + serverCert.getNotBefore());
        System.out.println("Valid until: " + serverCert.getNotAfter());
        System.out.println("Public Key: " + serverPublicKey);

        // Get streams to read/write data
        InputStream inputStream = socket.getInputStream();

        // Read data from the server
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);

        // Convert bytes to string
        String received = new String(buffer, 0, bytesRead);
        System.out.println("Server said: " + received);

        // Close connections
        inputStream.close();
        socket.close();
    }
}
