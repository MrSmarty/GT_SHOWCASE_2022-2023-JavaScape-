import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

class Server {

    private static final int PORT = 19;
    private static ArrayList<ServerThread> threads;

    private static BufferedReader keyboardReader;

    // Define socket and serverSocket to be used in completablefuture
    private static Socket socket;
    private static ServerSocket serverSocket;

    public static void main(String args[]) throws Exception {
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
                threads.add(new ServerThread(socket));
                threads.get(threads.size() - 1).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        CompletableFuture<Void> asyncInput = CompletableFuture.runAsync(() -> {
            try {
                // Get input
                String input = keyboardReader.readLine();
                System.out.println("Input: " + input);

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
                        threads.add(new ServerThread(socket));
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
                });

            }
        }

    }

    private static void processInput(String input) {
        if (input.charAt(0) == '/') {
            String[] args = input.substring(1).split(" ");
            if (args[0].equals("tellAll") && args.length > 1) {
                String temp = args[1];
                for (int i = 2; i < args.length; i++) {
                    temp += " " + args[i];
                }
                tellAll(temp);
                return;
            } else {
                error("Invalid Command");
            }
        }
    }

    private static void tellAll(String message) {
        for (ServerThread t : threads) {
            t.pushMessage(message);
        }
    }

    private static void error(String errorMessage) {
        System.out.print("\u001B[31m" + "ERROR: ");
        System.out.println(errorMessage + "\u001B[39m");
    }
}