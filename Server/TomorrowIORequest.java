import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;

public class TomorrowIORequest {


    private static String apiKey = "nhITGJgrQLaywQhgq233EVuNfKp6vGBk";
    private static String units = "imperial";
    private static String charset = "UTF-8";
    private static String baseURL = "https://api.tomorrow.io/v4/timelines";

    private static double[] latlong = { 30.425171640517842, -97.97453925002549 };

    public static double requestTemperature() {
        String query = String.format("location=%f,%f&fields=temperature&timesteps=current&units=%s&apikey=%s",
                latlong[0], latlong[1], units, apiKey);
        System.out.println(query);

        try {
            URLConnection connection = new URL(baseURL + "?" + query).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            System.out.println(baseURL + "?" + query);
            InputStream response = connection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println(responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public static void main(String[] args) throws IOException {
        requestTemperature();
    }

}
