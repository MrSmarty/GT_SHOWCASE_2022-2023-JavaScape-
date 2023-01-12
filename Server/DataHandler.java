import org.json.*;

public class DataHandler {
    public void createUser(String username, String password) {
        JSONObject user = new JSONObject();
        user.put("username", username);
        user.put("password", password);
        user.put("householdID", -1);
        
        
    }
}
