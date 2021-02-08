
import java.io.File;

public class AsiakkaatJaTilaukset {

    public static void main(String[] args) throws Throwable {
        File tiedosto = new File("db", "tilauskanta.db");
        Database database = new Database("jdbc:sqlite:" + tiedosto.getAbsolutePath());

        AsiakasDao asiakkaat = new AsiakasDao(database);
        TilausDao tilaukset = new TilausDao(database, asiakkaat);

        Tilaus t = tilaukset.findOne(4);
        System.out.println("Tilauksen teki: " + t.getAsiakas().getNimi());

    }

}
