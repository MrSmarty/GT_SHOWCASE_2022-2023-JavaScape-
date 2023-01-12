public class User {
    private String name = "";
    private String password = "";
    private int houseHoldID = -1;

    /**
     * Initializes a user with the name and password specified. Household ID
     * defaults to 0
     * 
     * @param name     Name of the User
     * @param password User's Password
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    /**
     * Initializes a user with the name, password, and Household ID specified
     * 
     * @param name        Name of the User
     * @param password    User's Password
     * @param houseHoldID The id of the household to assign the user to
     */
    public User(String name, String password, int houseHoldID) {
        this.name = name;
        this.password = password;
        this.houseHoldID = houseHoldID;
    }

    /**
     * Sets the household that this account is linked to
     * 
     * @param newID The new ID to set
     */
    public void setHouseHold(int newID) {
        this.houseHoldID = newID;
    }

    /**
     * Return the username of the user
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the password of the user
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }
}
