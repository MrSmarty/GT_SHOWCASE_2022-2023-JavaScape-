import java.net.*;
import java.io.*;

public class Client {
    private static final int PORT = 19;

    public static void main(String args[]) throws Exception {

        // Create client socket
        Socket socket = new Socket("localhost", PORT);

        // to send data to the server
        DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

        // to read data coming from the server
        BufferedReader bufferedIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // to read data from the keyboard
        BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
        String out;
        String in;

        // repeat as long as quit
        // is not typed at client
        boolean run = true;
        while (run) {

            out = keyboardReader.readLine();

            // send to the server
            if (out != null)
                dataOut.writeBytes(socket.getInetAddress().getHostName() + ": " + out + "\n");

            // receive from the server
            in = bufferedIn.readLine();

            System.out.println("[SERVER]: " + in);

            if (out.equalsIgnoreCase("quit"))
                run = false;
            out = null;
        }

        // close connection.
        dataOut.close();
        bufferedIn.close();
        keyboardReader.close();
        socket.close();
    }

}
