import java.io.*;
import java.net.*;
import java.util.concurrent.CompletableFuture;

public class ServerThread extends Thread {
    protected Socket socket;
    protected Server server;

    private PrintStream printStream;
    private BufferedReader clientReader;
    private BufferedReader keyboardReader;

    CompletableFuture<Void> asyncPrint;

    private String message = null;
    private String in = null;
    private String out = null;

    // Determines if while loop is running
    private boolean run = true;

    public ServerThread(Socket clientSocket, Server server) {
        this.socket = clientSocket;
        this.server = server;
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
            e.printStackTrace();
            return;
        }

        // Initialize async functions
        asyncPrint = CompletableFuture.runAsync(() -> {
            try {
                // give us the data coming in and print if not null
                in = clientReader.readLine();
                if (in != null)
                    System.out.println(in);

                if (in.contains("quit")) {
                    run = false;

                    server.cleanUp();

                    // close connection
                    printStream.close();
                    clientReader.close();
                    keyboardReader.close();
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        });

        // CompletableFuture<Void> asyncMessage = CompletableFuture.runAsync(() -> {
        // // Send keyboard out
        // // out = keyboardReader.readLine();
        // if (message != null) {
        // out = message;

        // // send to client
        // printStream.println(out);
        // printStream.flush();
        // System.out.println("Message Sent");
        // }
        // });

        // repeat as long as the client
        // does not send a null string

        // read from client
        while (run) {

            if (asyncPrint.isDone()) {
                asyncPrint = CompletableFuture.runAsync(() -> {
                    try {
                        // give us the data coming in and print if not null
                        in = clientReader.readLine();
                        System.out.println(in);
                        if (in == "quit") {
                            quit();
                            return;
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
                System.out.println("Message Sent");

                out = null;
                message = null;
            }

            // });
            // }

        }

    }

    public boolean pushMessage(String message) {
        this.message = message;
        return true;
    }

    private void quit() {
        run = false;

        asyncPrint.cancel(true);

        // close connection
        printStream.close();

        try {
            clientReader.close();
            keyboardReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}