public class ServerApp {
    public static void main(String[] args) throws Exception {
        Server server = new Server(19);
        ServerGUI g = new ServerGUI(server.getDataHandler());

        server.start();

    }
}
