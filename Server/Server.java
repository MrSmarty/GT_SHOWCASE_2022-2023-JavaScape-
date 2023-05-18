import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.*;
import java.time.*;
import com.google.gson.*;

import javafx.collections.*;

import java.lang.*;

/**
 * This is the classfile for the Server object
 * This class is responsible for running the server and handling all the threads
 */
public class Server {

    // The gson object to be used for saving and loading JSON
    private Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(ObservableList.class, new ObservableListDeserializer()).registerTypeAdapter(Reciever.class, new RecieverDeserializer()).registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).create();

    Debug Debug = new Debug();
    CommandParser commandParser = new CommandParser();

    // Autosave (defaults to true)
    private boolean autosave = true;
    // All the variables for the dataHandler and saving the JSON
    DataHandler dataHandler = null;
    String dataHandlerJSON;
    Path dataHandlerPath = Paths.get("dataHandler.json");
    File dataHandlerFile = new File(dataHandlerPath.toString());

    // The port to run the server on
    private int PORT = 19;
    // List of all the server threads
    private static ArrayList<ServerThread> threads;

    // Read input from the keyboard
    private static BufferedReader keyboardReader;

    // Define socket and serverSocket to be used in completablefuture
    private static Socket socket;
    private static ServerSocket serverSocket;

    /**
     * Initializes the server with port 19
     */
    public Server() {
        setUpDataHandler();
        saveDataHandler();
    }

    /**
     * Constructor for Server
     * 
     * @param PORT Initializes the server with a specified port number
     */
    public Server(int PORT) {
        this.PORT = PORT;
        setUpDataHandler();
        saveDataHandler();
    }

    public void setGUI(ServerGUI g) {

    }

    /**
     * Initializes the dataHandler object
     */
    private void setUpDataHandler() {
        try {
            if (dataHandlerFile.createNewFile()) {

                System.out.println("dataHandler.json created");
                dataHandler = new DataHandler();
                dataHandler.addUser(new User("admin", "admin", true));
                saveDataHandler();

            } else {
                System.out.println("dataHandler.json already exists");
                dataHandlerJSON = Files.lines(dataHandlerPath, StandardCharsets.UTF_8)
                        .collect(Collectors.joining("\n"));
                dataHandler = gson.fromJson(dataHandlerJSON, DataHandler.class);
                saveDataHandler();
            }

        } catch (IOException e) {
            System.out.println("Error reading dataHandler.json");

        }
    }

    /**
     * Saves the dataHandler object to a JSON file
     */
    public void saveDataHandler() {
        dataHandlerJSON = gson.toJson(dataHandler);
        try {
            FileWriter fileWriter = new FileWriter(dataHandlerPath.toString(), false);
            fileWriter.write(dataHandlerJSON);
            fileWriter.close();
            // System.out.println("dataHandler.json saved");
        } catch (IOException e) {
            System.out.println("Error writing to dataHandler.json");
        }
    }

    public Server getThis() {
        return this;
    }

    /**
     * begin running the server
     * 
     * @throws Exception
     */
    public void start() throws Exception {
        ServerApp.serverThreadCount += 1;

        threads = new ArrayList<ServerThread>();

        // Get input from keyboard
        keyboardReader = new BufferedReader(new InputStreamReader(System.in));

        // Create server Socket
        serverSocket = new ServerSocket(PORT);

        socket = null;

        CompletableFuture<Void> asyncSocket = null;
        CompletableFuture<Void> asyncInput = null;

        while (true) {

            // If async function is done, start again
            if (asyncSocket == null || asyncSocket.isDone()) {
                // Check sockets asyncronously
                System.out.println("Running Async Socket");
                asyncSocket = CompletableFuture.runAsync(() -> {
                    try {
                        System.out.println("Waiting for client to connect");
                        Socket clientSocket = null;
                        clientSocket = serverSocket.accept();
                        System.out.println("Client connected");

                        // new thread for a client
                        ServerThread t = new ServerThread(clientSocket, this);
                        threads.add(t);
                        Thread.ofVirtual().start(t);
                        //threads.get(threads.size() - 1).start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                if (autosave) {
                    saveDataHandler();
                }

            }
            

            // Start async funtion if not already doing so
            // if (asyncInput == null || asyncInput.isDone()) {

            //     // Check input asyncronously
            //     asyncInput = CompletableFuture.runAsync(() -> {
            //         try {
            //             // Get input
            //             String input = keyboardReader.readLine();

            //             if (input != null && input.length() > 0) {
            //                 processInput(input);
            //             }
            //         } catch (IOException e) {
            //             e.printStackTrace();
            //         }
            //         Debug.log("Async Input Completed");
            //     });

            //     if (autosave) {
            //         saveDataHandler();
            //     }

            // }
        }

    }

    /**
     * Process the input from the command line
     * 
     * @param input The string of input to process
     */
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
            } else if (args[0].equals("createUser")) {
                if (args.length == 3) {
                    // TODO: Make sure to finish this. Also, add GSON to the stuff
                    User newUser = new User(args[1], args[2]);
                    dataHandler.addUser(newUser);
                } else if (args.length == 4) {
                    HouseHold h = dataHandler.findHouseHold(args[3]);
                    User newUser = new User(args[1], args[2], h);
                    dataHandler.addUser(newUser);
                }
            } else if (args[0].equals("createHousehold")) {
                if (args.length == 2) {
                    HouseHold h = new HouseHold(args[1]);
                    dataHandler.addHouseHold(h);
                    System.out.println("Created Household: " + args[1]);
                } else {
                    System.out.println("Invalid Command");
                }
            } else if (args[0].equals("saveData")) {
                saveDataHandler();
                System.out.println("Saved Data");
            } else if (args[0].equals("getUsers")) {
                System.out.println(Arrays.toString(dataHandler.findHouseHold(args[1]).getAllUserNames()));
            } else if (args[0].equals("exit")) {
                System.exit(0);
            } else {
                error("Invalid Command");
            }
            if (autosave) {
                saveDataHandler();
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
                System.out.println("Pushing to Thread with ID: " + t.threadId());
            }
        }
    }

    /**
     * Send a command to every recieving client
     * 
     * @param command The command to send
     */
    private void commandAll(String command) {
        cleanUp();
        for (ServerThread t : threads) {
            if (t.getInfo() == 2) {
                t.pushCommand(command);
                System.out.println("Pushing to Thread with ID: " + t.threadId());
            }
        }
    }

    /**
     * Print an error message to the console
     * 
     * @param errorMessage The error message to print
     */
    private void error(String errorMessage) {
        System.out.print("\u001B[31m" + "ERROR: ");
        System.out.println(errorMessage + "\u001B[39m");
    }

    /**
     * Clean the threads that no longer have an active connection.
     * Needs to be run before any command that tries to connect using a socket
     * 
     * @return The number of threads removed
     */
    public int cleanUp() {

        int c = 0;
        // System.out.println("Cleaning...");
        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i).socket.isClosed()) {
                threads.remove(i);
                i--;
                c++;
            }
        }
        // System.out.println("Clean Complete! Removed " + c + " thread(s)!");
        return c;
    }

    /**
     * Returns the data handler
     * 
     * @return
     */
    public DataHandler getDataHandler() {
        if (dataHandler != null)
            return dataHandler;
        else
            System.out.println("No dataHandler found");
            return null;
    }

    public boolean authenticateAdmin(String username, String password) {
        return dataHandler.authenticateAdmin(username, password);
    }

    public boolean authenticate(String username, String password) {
        return dataHandler.authenticate(username, password);
    }

    /**
     * Get a count of the client and recieving threads used by the server
     * 
     * @return An array containing the number of client threads and recieving
     *         threads in that order
     */
    public int[] getThreadCount() {
        cleanUp();
        int[] count = new int[2];
        for (ServerThread t : threads) {
            if (t.getInfo() == 0 || t.getInfo() == 1) {
                count[0]++;
            } else if (t.getInfo() == 2) {
                count[1]++;
            }
        }
        System.out.println(count[0] + " | " + count[1]);
        return count;
    }

    public void updateRecieverConnectionStatus() {
        for (Reciever r : dataHandler.getRecievers()) {
            r.setOnline(false);
        }
        for (ServerThread t : threads) {
            //if ()
        }
    }

}