import java.util.*;

public class HouseHold {
    private int id = -1;
    private String name = "Household";
    private ArrayList<User> users = new ArrayList<User>();

    /**
     * Initializes a new household with the specified ID
     * 
     * @param id
     */
    public HouseHold(int id) {
        this.id = id;
    }

    /**
     * Initializes a new household with the specified name
     * 
     * @param name
     */
    public HouseHold(String name) {
        this.name = name;
    }

    /**
     * Initializes a new household with the specified ID and name
     * 
     * @param id
     * @param name
     */
    public HouseHold(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the ID of the household
     * 
     * @return the name of the household
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a user to the household
     * 
     * @param user
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Returns the user
     * 
     * @param username The String of the user to return
     * @return Returns the user if the user is found, otherwise return null
     */
    public User getUser(String username) {
        for (User u : users) {
            if (u.getName().equals(username)) {
                return u;
            }
        }
        return null;
    }

    public String[] getAllUserNames() {
        String[] toReturn = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            toReturn[i] = users.get(i).getName();
        }
        return toReturn;

    }
}
