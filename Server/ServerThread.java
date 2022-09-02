import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    protected Socket socket;

    private PrintStream printStream;
    private BufferedReader clientReader;
    private BufferedReader keyboardReader;

    private String message = null;

    public ServerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        printStream = null;
        clientReader = null;
        keyboardReader = null;

        try {
            // to send data to the client
            printStream = new PrintStream(socket.getOutputStream());

            // to read data coming from the client
            clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // to read data from the keyboard
            // keyboardReader = new BufferedReader(new InputStreamReader(System.in));

        } catch (IOException e) {
            return;
        }

        // server executes continuously
        while (true) {

            String in;
            String out;

            // repeat as long as the client
            // does not send a null string

            // read from client
            try {
                boolean run = true;
                while (run) {

                    // give us the data coming in and print if not null
                    in = clientReader.readLine();
                    System.out.println(in);

                    // Send keyboard out
                    // out = keyboardReader.readLine();
                    out = message;

                    // send to client
                    printStream.println(out);

                    // Reset value in and out
                    in = null;
                    out = null;
                    message = null;
                }

                // close connection
                printStream.close();
                clientReader.close();
                keyboardReader.close();
                socket.close();
            } catch (IOException e) {
                return;
            }

            // terminate application
            System.exit(0);

        } // end of while
    }

    public boolean pushMessage(String message) {
        // // Debug:
        System.out.println("Pushing Message...");

        // try {
        // out.writeBytes("[SERVER]: " + message + "\n\r");
        // out.flush();
        // return true;
        // } catch (Exception e) {
        // System.out.println("ERROR:");
        // e.printStackTrace();
        // return false;
        // }

        // this.message = message;
        return true;
    }
}