import java.util.*;

/**
 * This is the class that will be stored in JSON
 */
public class DataHandler {
    ArrayList<User> users = new ArrayList<User>();
    ArrayList<HouseHold> houseHolds = new ArrayList<HouseHold>();

    /**
     * Add a user to the database
     * 
     * @param newUser The USER to add
     */
    public void addUser(User newUser) {
        users.add(newUser);
    }

    public void addHouseHold(HouseHold newHouseHold) {
        houseHolds.add(newHouseHold);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Find a user by name
     * 
     * @param name The name of the user to find
     */
    public User findUser(String name) {
        for (User u : users) {
            if (u.getName().equals(name))
                return u;
        }
        return null;
    }

    public void deleteUser(User u) {
        users.remove(u);
    }

    public ArrayList<HouseHold> getHouseHolds() {
        return houseHolds;
    }

    /**
     * 
     * @param houseHoldName
     * @return the Household with the specified name. Returns
     *         <em>null</em> if no HouseHold is found.
     */
    public HouseHold findHouseHold(String houseHoldName) {
        for (HouseHold h : houseHolds) {
            if (h.getName().equals(houseHoldName))
                return h;
        }
        return null;
    }

    /**
     * 
     * @param houseHoldID
     * @return the Household with the specified ID (-1 is a valid ID). Returns <em>null</em> if no HouseHold is found.
     */
    public HouseHold findHouseHold(int houseHoldID) {
        for (HouseHold h : houseHolds) {
            if (h.getID() == houseHoldID)
                return h;
        }
        return null;
    }

    public boolean authenticateAdmin(String name, String password) {
        User u = findUser(name);
        if (u == null)
            return false;
        return u.getPassword().equals(password) && u.isAdmin();
    }

}
