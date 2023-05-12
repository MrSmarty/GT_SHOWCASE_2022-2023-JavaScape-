import java.util.ArrayList;

import com.google.gson.*;
import javafx.collections.*;

public class ObservableListDeserializer implements JsonDeserializer<ObservableList<?>> {

    @Override
    public ObservableList<?> deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        ObservableList<?> list = FXCollections.observableArrayList();

        // The array containing the elements to deserialize
        JsonArray jsonArray = json.getAsJsonArray();

        for (JsonElement element : jsonArray) {
            if (element.getAsJsonObject().has("name")) {
                list.add(context.deserialize(element, User.class));
            } else if (element.getAsJsonObject().has("houseHoldName")) {
                list.add(context.deserialize(element, HouseHold.class));
            } else if (element.getAsJsonObject().has("recieverName")) {
                list.add(context.deserialize(element, Reciever.class));
            }
        }

        return list;
    }

}
