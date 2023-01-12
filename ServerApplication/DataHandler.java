import java.util.*;

/**
 * This is the class that will be stored in JSON
 */
public class DataHandler {
    ArrayList<User> users = new ArrayList<User>();

    /**
     * Add a user to the database
     * @param newUser The USER to add
     */
    public void addUser(User newUser) {
        users.add(newUser);
    }

    /**
     * Find a user by name
     * @param name The name of the user to find
     */
    public User findUser(String name) {
        for (User u : users) {
            if (u.getName().equals(name))
                return u;
        }
        return null;
    }

}
