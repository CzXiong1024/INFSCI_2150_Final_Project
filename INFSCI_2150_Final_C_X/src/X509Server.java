import javax.net.ssl.*;
import java.io.*;

public class X509Server {
    public static void main(String[] args) throws Exception {
        // Set system properties for keystore
        System.setProperty("javax.net.ssl.keyStore", "src/X509Keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

        int port = 7999;
        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
        SSLSocket socket = (SSLSocket) serverSocket.accept();

        // Get streams to read/write data
        OutputStream outputStream = socket.getOutputStream();

        // Send a welcome message to the client
        outputStream.write("Welcome to the Chengzhuo's secure server with X.509 Certificate!".getBytes());

        // Close connections
        outputStream.close();
        socket.close();
        serverSocket.close();
    }
}
