import java.util.ArrayList;

import com.google.gson.*;
import javafx.collections.*;

public class ObservableListDeserializer implements JsonDeserializer<ObservableList<?>> {

    @Override
    public ObservableList<User> deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        ObservableList<User> list = FXCollections.observableArrayList();

        // The array containing the elements to deserialize
        JsonArray jsonArray = json.getAsJsonArray();

        for (JsonElement element : jsonArray) {
            list.add(context.deserialize(element, User.class));
        }

        return list;
    }

}
