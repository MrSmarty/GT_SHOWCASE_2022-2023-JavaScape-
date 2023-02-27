import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class UserListItem {
    Server server;
    ModalManager modalManager;

    public UserListItem(Server s, ModalManager m) {
        server = s;
        modalManager = m;
    }

    /**
     * The list of Users with their name, email, and a way to edit said Users
     * 
     * @return
     */
    public GridPane getUserListItem(User user) {
        GridPane g = new GridPane();
        Text username = new Text(user.getName());
        Text email = new Text(user.getEmail());
        Button edit = new Button();
        edit.setOnAction(e -> {
            System.out.println("Editing user");
            modalManager.editUserModal(user);
        });
        Button delete = new Button("Delete");
        delete.setOnAction(e -> {
            System.out.println("Deleting user");
            user.delete(server);
        });

        g.add(username, 0, 0);
        g.add(email, 0, 1);
        g.add(edit, 1, 0);
        g.add(delete, 2, 0);

        return g;

    }
}
