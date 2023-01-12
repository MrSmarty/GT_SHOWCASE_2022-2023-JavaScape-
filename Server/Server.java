import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.*;
import com.google.gson.*;

class Server {
    final Path dataHandlerPath = Paths.get("dataHandler.json");

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Debug Debug = new Debug();
    CommandParser commandParser = new CommandParser();

    DataHandler dataHandler;
    String dataHandlerJSON;

    private int PORT = 19;
    private static ArrayList<ServerThread> threads;

    private static BufferedReader keyboardReader;

    // Define socket and serverSocket to be used in completablefuture
    private static Socket socket;
    private static ServerSocket serverSocket;

    public Server(int PORT) {
        this.PORT = PORT;
    }

    public void start() throws Exception {

        try {
            dataHandlerJSON = Files.lines(dataHandlerPath, StandardCharsets.UTF_8)
                    .collect(Collectors.joining("\n"));
            if (dataHandlerJSON == null || dataHandlerJSON.equals("")) {
                dataHandler = new DataHandler();
                dataHandlerJSON = gson.toJson(dataHandler);
            } else
                dataHandler = gson.fromJson(dataHandlerJSON, DataHandler.class);
        } catch (IOException e) {

        }

        threads = new ArrayList<ServerThread>();

        // Get input from keyboard
        keyboardReader = new BufferedReader(new InputStreamReader(System.in));

        // Create server Socket
        serverSocket = new ServerSocket(PORT);

        socket = null;

        CompletableFuture<Void> asyncSocket = CompletableFuture.runAsync(() -> {
            try {
                socket = serverSocket.accept();

                // new thread for a client
                threads.add(new ServerThread(socket, this));
                threads.get(threads.size() - 1).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        CompletableFuture<Void> asyncInput = CompletableFuture.runAsync(() -> {
            try {
                // Get input
                String input = keyboardReader.readLine();

                if (input != null && input.length() > 0) {
                    processInput(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        while (true) {

            // If async function is done, start again
            if (asyncSocket.isDone()) {
                // Check sockets asyncronously
                asyncSocket = CompletableFuture.runAsync(() -> {
                    try {
                        socket = serverSocket.accept();

                        // new thread for a client
                        threads.add(new ServerThread(socket, this));
                        threads.get(threads.size() - 1).start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }

            // Start async funtion if not already doing so
            if (asyncInput.isDone()) {

                // Check input asyncronously
                asyncInput = CompletableFuture.runAsync(() -> {
                    try {
                        // Get input
                        String input = keyboardReader.readLine();

                        if (input != null && input.length() > 0) {
                            processInput(input);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Debug.log("Async Input Completed");
                });

            }
        }

    }

    private void processInput(String input) {
        if (input.charAt(0) == '/') {
            String[] args = input.substring(1).split(" ");

            if (args[0].equals("tellAll") && args.length > 1) { // /tellAll
                String temp = args[1];
                for (int i = 2; i < args.length; i++) {
                    temp += " " + args[i];
                }
                tellAll(temp);
                return;
            } else if (args[0].equals("commandAll") && args.length > 1) { // /commandAll
                String temp = args[1];
                for (int i = 2; i < args.length; i++) {
                    temp += " " + args[i];
                }
                commandAll(temp);
                return;
            } else if (args[0].equals("toggleDebug") && (args[1].equals("true") || args[1].equals("false"))) {
                Debug.setMode(Boolean.parseBoolean(args[1]));
            } else {
                error("Invalid Command");
            }
        }
    }

    /**
     * Send command to all user clients
     * 
     * @param message the message to send
     */
    private void tellAll(String message) {
        cleanUp();
        for (ServerThread t : threads) {
            if (t.getInfo() == 0 || t.getInfo() == 1) {
                t.pushMessage(message);
                System.out.println("Pushing to Thread with ID: " + t.getId());
            }
        }
    }

    /**
     * Send a command to every recieving client
     */
    private void commandAll(String command) {
        cleanUp();
        for (ServerThread t : threads) {
            if (t.getInfo() == 2) {
                t.pushCommand(command);
                System.out.println("Pushing to Thread with ID: " + t.getId());
            }
        }
    }

    /**
     * Print an error message to the console
     */
    private void error(String errorMessage) {
        System.out.print("\u001B[31m" + "ERROR: ");
        System.out.println(errorMessage + "\u001B[39m");
    }

    /**
     * Clean the threads that no longer have a connection
     */
    public void cleanUp() {
        int c = 0;
        System.out.println("Cleaning...");
        for (ServerThread st : threads) {
            if (st.socket.isClosed()) {
                threads.remove(st);
                c++;
            }
        }
        System.out.println("Clean Complete! Removed " + c + " thread(s)!");
    }

}