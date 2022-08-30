import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws IOException {
        String address = "localhost";
        int port = 19;

        // Sends a socket request to the server
        Socket s = new Socket(address, port);
    }
}
