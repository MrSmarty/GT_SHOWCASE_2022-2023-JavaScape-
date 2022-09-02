import java.io.*;
import java.net.*;
import java.util.*;

class Server {

    private static final int PORT = 19;
    private static ArrayList<ServerThread> threads;

    private static BufferedReader keyboardReader;

    public static void main(String args[]) throws Exception {
        threads = new ArrayList<ServerThread>();

        // Get input from keyboard
        keyboardReader = new BufferedReader(new InputStreamReader(System.in));

        // Create server Socket
        ServerSocket serverSocket = new ServerSocket(PORT);

        Socket socket = null;

        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            threads.add(new ServerThread(socket));
            threads.get(threads.size() - 1).start();

            // Get input
            String input = keyboardReader.readLine();

            if (input != null) {
                processInput(input);
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
                System.out.println("ERROR: INVALID COMMAND");
            }
        }
    }

    private static void tellAll(String message) {
        for (ServerThread t : threads) {
            t.pushMessage(message);
        }
    }
}