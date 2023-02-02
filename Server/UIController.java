public class UIController {
    private Server server;

    public UIController(Server server) {
        this.server = server;
    }

    public boolean authenticate(String username, String password) {
        return server.authenticate(username, password);
    }
}
