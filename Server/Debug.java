public class Debug {

    boolean debugMode = false;

    public void log(String log) {
        if (debugMode)
            System.out.println("LOG: " + log);
    }

    public void warn(String warn) {
        if (debugMode)
            System.out.println("WARNING: " + warn);
    }

    public void error(String error) {
        if (debugMode)
            System.out.println("ERROR: " + error);
    }

    public void setMode(boolean mode) {
        debugMode = mode;
    }
}
