public class User {
    /**
     * Admins have access to the server
     */
    private boolean isAdmin = false;
    private String name = "";
    private String password = "";
    private String email = "";
    private int householdID = -1;

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
    public User(String name, String password, HouseHold houseHold) {
        this.name = name;
        this.password = password;
        houseHold.addUser(this);
        this.householdID = houseHold.getID();
    }

    /**
     * Initializes a user with the name, password, and Household ID specified
     * 
     * @param name        Name of the User
     * @param password    User's Password
     * @param houseHoldID The id of the household to assign the user to
     * @param isAdmin     Is the user an admin?
     */
    public User(String name, String password, HouseHold houseHold, boolean isAdmin) {
        this.name = name;
        this.password = password;
        houseHold.addUser(this);
        this.isAdmin = isAdmin;
    }

    /**
     * Initializes a user with the name, password, and Household ID specified
     * 
     * @param name        Name of the User
     * @param password    User's Password
     * @param houseHoldID The id of the household to assign the user to
     * @param isAdmin     Is the user an admin?
     */
    public User(String name, String password, String email, HouseHold houseHold, boolean isAdmin) {
        this.name = name;
        this.password = password;
        this.email = email;
        houseHold.addUser(this);
        this.isAdmin = isAdmin;
    }

    /**
     * Sets the household that this account is linked to
     * 
     * @param newID The new ID to set
     */
    public void setHouseHold(HouseHold newHouseHold) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void delete(Server s) {
        s.getDataHandler().findHouseHold(householdID).deleteUser(this);
        s.getDataHandler().deleteUser(this);
        
    }

    /**
     * Returns true if the user is an admin
     * 
     * @return
     */
    public boolean isAdmin() {
        return isAdmin;
    }
}
