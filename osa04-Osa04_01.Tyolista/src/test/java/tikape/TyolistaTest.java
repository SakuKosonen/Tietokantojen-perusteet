package tikape;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

@Points("04-01")
public class TyolistaTest extends FluentTest {

    public WebDriver webDriver = new HtmlUnitDriver();

    @Override
    public WebDriver getDefaultDriver() {
        return webDriver;
    }

    @ClassRule
    public static ServerRule server = new ServerRule();

    @Test
    public void contentIsAddedOnFormSubmit() {
        canAddContent();
    }

    @Test
    public void contentCanBeAddedSeveralTimes() {
        canAddContent(5);
    }

    private void canAddContent() {
        canAddContent(1);
    }
    
    private void canAddContent(int times) {
        List<String> content = new ArrayList<>();
        goTo("http://localhost:" + server.getPort() + "/");

        for (int i = 0; i < times; i++) {
            String toAdd = UUID.randomUUID().toString().substring(0, 6);

            assertThat(pageSource()).doesNotContain(toAdd);

            fill("input[type=text]").with(toAdd);
            click("input[type=submit]");
            assertThat(pageSource()).contains(toAdd);
        }

        for (String c : content) {
            assertThat(pageSource()).contains(c);
        }
    }

}
