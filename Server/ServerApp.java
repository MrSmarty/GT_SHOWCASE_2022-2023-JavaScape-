public class ServerApp {
    public static void main(String[] args) throws Exception {
        Server server = new Server(19);
        GUI g = new GUI();

        server.start();

    }
}
