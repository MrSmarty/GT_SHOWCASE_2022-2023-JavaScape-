import java.util.ArrayList;

import com.google.gson.*;
import javafx.collections.*;

public class RecieverDeserializer implements JsonDeserializer<Reciever> {

    @Override
    public Reciever deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        Reciever r;
        JsonObject j = json.getAsJsonObject();
        String name = j.get("subType").toString();
        name = name.substring(1, name.length()-1);

        try {
            r = context.deserialize(json, Class.forName(name));
        } catch (Exception e) {
            System.out.println("Exception in Reciever Deserializer");
            System.out.println(name);
            e.printStackTrace();
            return null;
        }
        

        return r;
    }

}
