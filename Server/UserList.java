import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class UserList {
    Server server;

    public UserList(Server s) {
        server = s;
    }

    /**
     * The list of Users with their name, email, and a way to edit said Users
     * @return
     */
    public ScrollPane getUserList() {
        GridPane userGrid = new GridPane();
        ScrollPane userList = new ScrollPane(userGrid);
        int i = 0;
        for (User u : server.getDataHandler().getUsers()) {
            GridPane g = new GridPane();
            Text username = new Text(u.getName());
            Text email = new Text(u.getEmail());
            Button edit = new Button();
            Button delete = new Button("Delete");
            delete.setOnAction(e -> {
                System.out.println("Deleting user");
                u.delete(server);
            });

            g.add(username, 0, 0);
            g.add(email, 0, 1);
            g.add(delete, 1, 0);

            userGrid.add(g, 0, i);
            i++;

        }
        return userList;
    }
}
