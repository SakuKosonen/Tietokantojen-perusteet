
import com.google.gson.Gson;
import java.io.File;
import spark.Spark;

public class Main {

    public static void main(String[] args) throws Throwable {
        File tiedosto = new File("db", "todot.db");
        Database database = new Database("jdbc:sqlite:" + tiedosto.getAbsolutePath());

    }

}
