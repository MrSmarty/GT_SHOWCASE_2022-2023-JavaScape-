import java.net.*;
//import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class HeadlessClient {
    private String ip;
    private int port;

    private boolean useGui;
    private GUI gui;

    private LocalDateTime dateTime = LocalDateTime.now();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("[mm/dd/yy | hh:mm:ss]: ");

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

    public HeadlessClient(String ip, int port, boolean gui) {
        this.ip = ip;
        this.port = port;
        this.useGui = gui;
    }

    public void start() throws Exception {

        // Create client socket
        socket = new Socket(ip, port);

        // to send data to the server
        dataOut = new DataOutputStream(socket.getOutputStream());

        // to read data coming from the server
        bufferedIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // to read data from the keyboard
        keyboardReader = new BufferedReader(new InputStreamReader(System.in));

        dataOut.writeBytes(
                dateTime.format(dateFormatter) + socket.getInetAddress().getHostName() + ": Connected" + "\n");
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
                            dataOut.writeBytes(dateTime.format(dateFormatter) + socket.getInetAddress().getHostName()
                                    + ": " + out + "\n");

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

                        if (in.equals("getType")) {
                            dataOut.writeBytes("type 1\n");
                            dataOut.flush();
                        } else if (in != null)
                            System.out.println(in);

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
