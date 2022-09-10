import java.net.*;
import java.util.concurrent.CompletableFuture;
import java.io.*;

public class Client {
    private String ip;
    private int port;

    Socket socket;
    DataOutputStream dataOut;
    BufferedReader bufferedIn;
    BufferedReader keyboardReader;

    CompletableFuture<Void> asyncInput;
    CompletableFuture<Void> asyncPrint;

    // Strings to contain input and output
    String out;
    String in;

    // True if while loop should be running
    boolean run = true;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() throws Exception {

        // Create client socket
        socket = new Socket("localhost", 19);

        // to send data to the server
        dataOut = new DataOutputStream(socket.getOutputStream());

        // to read data coming from the server
        bufferedIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // to read data from the keyboard
        keyboardReader = new BufferedReader(new InputStreamReader(System.in));

        dataOut.writeBytes("Connected");
        dataOut.flush();

        // repeat as long as quit
        // is not typed at client
        while (run) {

            if (asyncInput == null || asyncInput.isDone()) {
                // Start asynchronous fucntion to handle keyboard input
                asyncInput = CompletableFuture.runAsync(() -> {
                    try {
                        out = keyboardReader.readLine();

                        if (out.equalsIgnoreCase("quit")) {
                            quit();
                            return;
                        }

                        // send to the server
                        if (out != null && !out.equals("") && run != false)
                            dataOut.writeBytes(socket.getInetAddress().getHostName() + ": " + out + "\n");

                        out = null;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
            }

            if (asyncPrint == null || asyncPrint.isDone()) {
                asyncPrint = CompletableFuture.runAsync(() -> {
                    try {

                        // Recieve from server
                        in = bufferedIn.readLine();

                        if (in != null)
                            System.out.println("[SERVER]: " + in);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        }

    }

    private void quit() {
        try {
            dataOut.writeBytes("quit");
        } catch (IOException e) {
            System.out.println("Exception in writing to dataOut");
        }
        run = false;
        System.out.println("Exiting...");

        asyncInput.cancel(true);
        asyncPrint.cancel(true);

        // close connection.
        try {
            dataOut.close();
            bufferedIn.close();
            keyboardReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void setMessage(String message) {

    }

}
