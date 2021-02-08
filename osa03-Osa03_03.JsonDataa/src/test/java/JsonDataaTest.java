
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import fi.helsinki.cs.tmc.edutestutils.Points;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.io.File;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.ClassRule;

@Points("03-03")
public class JsonDataaTest {

    @ClassRule
    public static ServerRule server = new ServerRule();

    @Test
    public void getReturnsListWithContentFromDatabase() throws SQLException {
        tietokantaTiedostoOlemassa();

        Response res = RestAssured.get("http://localhost:" + server.getPort()).thenReturn();
        assertTrue("Jotain meni pieleen kun palvelimelta haettiin JSON-dataa. Statuskoodi " + res.getStatusCode(), res.getStatusCode() == 200);

        Type todoObjectType = new TypeToken<List<Todo>>() {
        }.getType();
        Gson gson = new Gson();
        List<Todo> todot = gson.fromJson(res.getBody().asString(), todoObjectType);

        String task = UUID.randomUUID().toString().substring(0, 6);
        boolean b = new Random().nextBoolean();

        assertEquals(0, todot.stream().filter(t -> t.tehtava != null && t.tehtava.equals(task) && t.tehty != null && t.tehty.equals(b)).count());

        addToDatabase(task, b);

        has(task, b);
    }

    @Test
    public void postAddsToDatabase() throws SQLException {
        tietokantaTiedostoOlemassa();

        String task = UUID.randomUUID().toString().substring(0, 6);
        boolean b = new Random().nextBoolean();

        Todo t = new Todo(null, task, b);

        Response res = RestAssured.given().body(new Gson().toJson(t)).post("http://localhost:" + server.getPort()).thenReturn();

        has(task, b);

    }

    public void has(String task, Boolean done) {
        Type todoObjectType = new TypeToken<List<Todo>>() {
        }.getType();

        Response res = RestAssured.get("http://localhost:" + server.getPort()).thenReturn();
        assertTrue("Jotain meni pieleen kun palvelimelta haettiin JSON-dataa. Statuskoodi " + res.getStatusCode(), res.getStatusCode() == 200);

        List<Todo> todot = new Gson().fromJson(res.getBody().asString(), todoObjectType);
        assertEquals(1, todot.stream().filter(t -> t.tehtava != null && t.tehtava.equals(task) && t.tehty != null && t.tehty.equals(done)).count());
    }

    public void addToDatabase(String task, Boolean done) throws SQLException {
        tietokantaTiedostoOlemassa();

        File f = new File("db", "todot.db");

        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + f.getAbsolutePath());

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Todo (tehtava, tehty) VALUES (?, ?)");
        stmt.setString(1, task);
        stmt.setBoolean(2, done);
        stmt.executeUpdate();

        conn.close();
    }

    public void tietokantaTiedostoOlemassa() {
        File f = new File("db", "todot.db");
        if (!f.exists()) {
            fail("Tiedostoa todot.db ei löydy kansiosta db.");
        }

    }

}
