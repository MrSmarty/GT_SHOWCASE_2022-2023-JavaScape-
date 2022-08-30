import java.io.*;
import java.net.*;
import java.sql.*;

public class Server {
    public static void Main(String[] args) throws IOException {
        // Hosts server at port
        ServerSocket serverSocket = new ServerSocket(19);

        // Accepts the socket connections
        Socket socket = serverSocket.accept();
    }
}