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
    private int type = -1;

    // Determines if while loop is running
    private boolean run = true;

    public ServerThread(Socket clientSocket, Server server) {
        this.socket = clientSocket;
        this.server = server;
    }

    public void run() {
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

        message = "getType";

        // repeat as long as the client
        // does not send a null string

        // read from client
        while (run) {

            if (asyncPrint == null || asyncPrint.isDone()) {
                asyncPrint = CompletableFuture.runAsync(() -> {
                    try {
                        // give us the data coming in and print if not null
                        in = socketReader.readLine();
                        // if (in == null)
                        // return;

                        System.out.println(in);
                        if (in.equals("quit")) {
                            quit();
                            return;
                        } else if (in.substring(0, 4).equals("type")) {
                            System.out.println("Got type");
                            type = Integer.parseInt(in.substring(5));
                            System.out.println("Type is now: " + type);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    in = null;
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

            // });
            // }

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
        // TODO: printStream.close();

        try {
            // TODO: clientReader.close();
            keyboardReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getType() {
        return type;
    }
}