package auds.Aud10DesignPatterns.Singleton;

class Logger {
    StringBuilder sb = new StringBuilder();
    static Logger instance;

    private Logger() {
        sb = new StringBuilder();
    }

    public static Logger getInstance() {
        // eve go dodeluvanjeto na instanca t.e Singleton implementacijata.
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    public void addLog(String log){
        sb.append(log).append("\n"); // prajme log, na sekoj log dodavame nov red. Site "objekti" ce go ima istio log.
    }
    public String toString(){
        return sb.toString();
    }

}

public class LoggerTest {
    public static void main(String[] args) {

      //  Logger logger = new Logger(); // tuka, so private Logger, ne mozime da instancirame objekt.
        Logger l1 = Logger.getInstance();
        Logger l2 = Logger.getInstance();

        l1.addLog("Test1");
        l2.addLog("Test2");

        System.out.println(l1);
        System.out.println(l2);
        // prints out
        // Test1
        // Test2

        //Test1
        //Test2
    }
}
