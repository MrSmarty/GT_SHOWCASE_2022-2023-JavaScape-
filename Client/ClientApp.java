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
    }

    public static void createProperties(File propertiesFile) throws IOException {
        Properties temp = new Properties();
        OutputStream tempOut = null;

        tempOut = new FileOutputStream(propertiesFile);

        temp.setProperty("rememberMe", "false");
        temp.setProperty("username", "");
        temp.setProperty("password", "");
        temp.setProperty("IP", "");
        temp.setProperty("port", "");

        temp.store(tempOut, null);

        tempOut.close();
    }
}
