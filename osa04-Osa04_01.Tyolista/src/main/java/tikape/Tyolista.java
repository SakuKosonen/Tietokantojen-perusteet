package tikape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Tyolista {

    public static void main(String[] args) throws Exception {
        System.out.println("Hei maailma!");

        List<Tehtava> tehtavat = new ArrayList<>();
        tehtavat.add(new Tehtava("Lisää lomake"));
        tehtavat.add(new Tehtava("Lisää tiedon lähetystä kuunteleva osoite"));
        tehtavat.add(new Tehtava("Tallenna lähetetty tieto"));
        tehtavat.add(new Tehtava("..."));

        Spark.get("*", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("tehtavat", tehtavat);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.post("*", (req, res) -> {
            String nimi = req.queryParams("tehtava");

            tehtavat.add(new Tehtava(nimi));

            res.redirect("*");
            return "";
        });

    }
}
