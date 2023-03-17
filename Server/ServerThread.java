import java.io.*;
import java.net.*;
import java.util.concurrent.CompletableFuture;
import java.time.*;
import java.time.format.*;

public class ServerThread extends Thread {
    protected Socket socket;
    protected Server server;

    private LocalDateTime dateTime = LocalDateTime.now();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("[mm/dd/yy | hh:mm:ss]: ");

    private PrintStream printStream;
    private BufferedReader socketReader;
    private BufferedReader keyboardReader;

    CompletableFuture<Void> asyncPrint;

    private String message = null;
    private String in = null;
    private String out = null;

    // -1 is null
    // 0 is client
    // 1 is headless client
    // 2 is reciever
    /**
     * Determines the type of thread. -1 is null, 0 is client, 1 is headless client,
     * 2 is a reciever
     */
    private int type = -1;
    /**
     * Determines the id of the reciever. -1 is null
     * use *BIG* encoding when getting from reciever
     */
    private int id = -1;

    // Determines if while loop is running
    private boolean run = true;

    public ServerThread(Socket clientSocket, Server server) {
        this.socket = clientSocket;
        this.server = server;
    }

    public void run() {
        System.out.println("New thread created");
        printStream = null;
        socketReader = null;
        keyboardReader = null;

        try {
            // to send data to the client
            printStream = new PrintStream(socket.getOutputStream());

            // to read data coming from the client
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // to read data from the keyboard
            // keyboardReader = new BufferedReader(new InputStreamReader(System.in));

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        message = "getInfo";

        // repeat as long as the client
        // does not send a null string

        // read from client
        while (run) {

            if (asyncPrint == null || asyncPrint.isDone()) {
                asyncPrint = CompletableFuture.runAsync(() -> {
                    try {
                        // give us the data coming in and print if not null
                        in = socketReader.readLine();
                        if (in == null)
                            return;

                        System.out.println(in);
                        String[] arguments = in.split(" ");
                        if (arguments[0].equals("quit")) {
                            quit();
                            return;
                        } else if (arguments[0].equals("type")) {
                            System.out.println("Got Info");
                            type = Integer.parseInt(arguments[1]);
                            System.out.println("Type is now: " + type);
                            if (type == 2) {
                                id = Integer.parseInt(arguments[2]);
                                System.out.println("ID is now: " + id);
                            }
                        }
                        in = null;
                    } catch (IOException e) {
                        System.out.println("IOException | Closing thread");
                        quit();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                });
            }

            // if (asyncMessage.isDone()) {
            // asyncMessage = CompletableFuture.runAsync(() -> {
            // Send keyboard out
            // out = keyboardReader.readLine();
            if (message != null && run != false) {
                out = message;

                // send to client
                printStream.println(out);
                printStream.flush();
                System.out.println("Message Sent:" + out);

                out = null;
                message = null;
            }

        }

    }

    public boolean pushMessage(String message) {
        this.message = dateTime.format(dateFormatter) + " [SERVER] " + message;
        return true;
    }

    public boolean pushCommand(String command) {
        this.message = command;
        return true;
    }

    private void quit() {
        run = false;

        asyncPrint.cancel(true);

        // close connection
        try {
            keyboardReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the type of thread. -1 is null, 0 is client, 1 is headless client, 2
     * is a reciever
     * 
     * @return the int for the tye of connection.
     */
    public int getInfo() {
        return type;
    }
}