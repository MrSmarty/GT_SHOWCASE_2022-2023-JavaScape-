import java.io.*;
import java.util.*;

public class ClientApp {

    public static void main(String[] args) throws IOException {

        File configFile = new File("config.properties");
        if (!configFile.isFile()) {
            createProperties(configFile);
        }

        // Reader to read config.properties
        FileReader configReader = new FileReader("config.properties");

        Properties p = new Properties();

        p.load(configReader);

        System.out.println("Conecting to ip: " + p.getProperty("IP") + " and port: " + p.getProperty("port"));

        Client client = new Client(p.getProperty("IP"), Integer.parseInt(p.getProperty("port")));
        try {
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createProperties(File propertiesFile) throws IOException {
        Properties temp = new Properties();
        OutputStream tempOut = null;

        tempOut = new FileOutputStream(propertiesFile);

        temp.setProperty("rememberMe", "false");
        temp.setProperty("username", "");
        temp.setProperty("password", "");
        temp.setProperty("IP", "localhost");
        temp.setProperty("port", "19");

        temp.store(tempOut, null);

        tempOut.close();
    }
}
