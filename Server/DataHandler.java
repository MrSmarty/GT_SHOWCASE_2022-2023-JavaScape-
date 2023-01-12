import java.util.*;

public class DataHandler {
    ArrayList<User> users = new ArrayList<User>();

    public User findUser(String name) {
        for (User u : users) {
            if (u.getName().equals(name))
                return u;
        }
        return null;
    }

}
