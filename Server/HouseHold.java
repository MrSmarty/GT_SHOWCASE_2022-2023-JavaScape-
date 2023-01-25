public class HouseHold {
    private int id = -1;
    private String name = "Household";

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
     * @param name
     */
    public HouseHold(String name) {
        this.name = name;
    }

    /**
     * Initializes a new household with the specified ID and name
     * @param id
     * @param name
     */
    public HouseHold(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the ID of the household
     * @return the name of the household
     */
    public String getName() {
        return name;
    }
}
