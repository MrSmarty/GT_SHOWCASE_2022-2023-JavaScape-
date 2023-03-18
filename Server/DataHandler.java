import java.util.*;
import javafx.*;
import javafx.collections.*;
import javafx.scene.*;

/**
 * This is the class that will be stored in JSON
 */
public class DataHandler {

    private Settings settings = new Settings();

    private int houseHoldIDIndex = 0;

    ObservableList<User> users = FXCollections.observableArrayList();
    ObservableList<HouseHold> houseHolds = FXCollections.observableArrayList();
    ObservableList<Reciever> recievers = FXCollections.observableArrayList();

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

    public void addReciever(Reciever newReciever) {
        recievers.add(newReciever);
    }

    public ObservableList<User> getUsers() {
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

    public ObservableList<HouseHold> getHouseHolds() {
        return houseHolds;
    }

    public int getHouseHoldIDIndex() {
        return houseHoldIDIndex++;
    }

    /**
     * 
     * @param houseHoldName
     * @return the Household with the specified name. Returns
     *         <em>null</em> if no HouseHold is found.
     */
    public HouseHold findHouseHold(String houseHoldName) {
        for (HouseHold h : houseHolds) {
            if (h.getHouseHoldName().equals(houseHoldName))
                return h;
        }
        return null;
    }

    /**
     * 
     * @param houseHoldID
     * @return the Household with the specified ID (-1 is a valid ID). Returns
     *         <em>null</em> if no HouseHold is found.
     */
    public HouseHold findHouseHold(int houseHoldID) {
        for (HouseHold h : houseHolds) {
            if (h.getID() == houseHoldID)
                return h;
        }
        return null;
    }

    public void deleteHouseHold(HouseHold h) {
        houseHolds.remove(h);
    }

    public ObservableList<Reciever> getRecievers() {
        return recievers;
    }

    public Reciever findReciever(int id) {
        for (Reciever r : recievers) {
            if (r.getID() == id)
                return r;
        }
        return null;
    }

    public void deleteReciever(Reciever r) {
        recievers.remove(r);
    }

    public boolean authenticateAdmin(String name, String password) {
        User u = findUser(name);
        if (u == null)
            return false;
        return u.getPassword().equals(password) && u.isAdmin();
    }

    public boolean authenticate(String name, String password) {
        User u = findUser(name);
        if (u == null)
            return false;
        return u.getPassword().equals(password);
    }

    public Settings getSettings() {
        return settings;
    }

}
