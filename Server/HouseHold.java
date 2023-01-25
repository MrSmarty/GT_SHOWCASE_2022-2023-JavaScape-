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
     * @param user
     */
    public void addUser(User user) {
        users.add(user);
    }
}
