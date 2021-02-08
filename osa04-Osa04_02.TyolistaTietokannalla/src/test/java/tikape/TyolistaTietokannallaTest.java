package tikape;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.fluentlenium.adapter.FluentTest;
import static org.junit.Assert.assertTrue;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

@Points("04-02")
public class TyolistaTietokannallaTest extends FluentTest {

    public WebDriver webDriver = new HtmlUnitDriver();

    @Override
    public WebDriver getDefaultDriver() {
        return webDriver;
    }

    @ClassRule
    public static ServerRule server = new ServerRule();

    @Test
    public void contentIsAddedOnFormSubmit() throws SQLException {
        canAddContent();
    }

    @Test
    public void contentCanBeAddedSeveralTimes() throws SQLException {
        canAddContent(5);
    }

    private void canAddContent() throws SQLException {
        canAddContent(1);
    }

    private void canAddContent(int times) throws SQLException {
        List<String> content = new ArrayList<>();
        goTo("http://localhost:" + server.getPort() + "/");

        for (int i = 0; i < times; i++) {
            String toAdd = UUID.randomUUID().toString().substring(0, 6);

            assertThat(pageSource()).doesNotContain(toAdd);

            fill("input[type=text]").with(toAdd);
            click("input[type=submit]");
            assertThat(pageSource()).contains(toAdd);
            
            assertTrue(databaseHasContent(toAdd));
        }

        for (String c : content) {
            assertThat(pageSource()).contains(c);
        }
    }

    public boolean databaseHasContent(String content) throws SQLException {
        File dbFile = new File("db", "tasks.db");
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = conn.prepareStatement("SELECT nimi FROM Tehtava");
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                if (content.equals(res.getString("nimi"))) {
                    return true;
                }
            }
        }

        return false;
    }

}
