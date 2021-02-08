
import org.junit.Test;
import org.junit.Rule;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Points("03-02")
public class AsiakkaatJaTilauksetTest {

    @Rule
    public MockStdio io = new MockStdio();

    @Test
    public void asiakkaidenLisaysJaListaus() throws ClassNotFoundException, SQLException {
        File tmp = new File("db", "tmp-" + UUID.randomUUID().toString());
        initDbs(tmp);

        Database db = new Database("jdbc:sqlite:" + tmp.getAbsolutePath());

        AsiakasDao dao = new AsiakasDao(db);

        dao.saveOrUpdate(new Asiakas(null, "Asiakas", "123-123123", "123123", 13000, "Tampere"));
        dao.saveOrUpdate(new Asiakas(null, "Toinen", "124-123124", "54321", 100, "Helsinki"));

        Asiakas rnd = new Asiakas(null, UUID.randomUUID().toString().substring(0, 6), "1", "1", 1, "1");
        dao.saveOrUpdate(rnd);

        List<Asiakas> asiakkaat = dao.findAll();

        assertNotNull(asiakkaat);
        assertEquals(3, asiakkaat.size());

        assertEquals(1, asiakkaat.stream().filter(a -> a.id != null && a.id > 0 && a.nimi.equals("Asiakas") && a.puhelinnumero.equals("123-123123") && a.katuosoite.equals("123123") && a.postinumero.equals(13000) && a.postitoimipaikka.equals("Tampere")).count());
        assertEquals(1, asiakkaat.stream().filter(a -> a.id != null && a.id > 0 && a.nimi.equals("Toinen") && a.puhelinnumero.equals("124-123124") && a.katuosoite.equals("54321") && a.postinumero.equals(100) && a.postitoimipaikka.equals("Helsinki")).count());
        assertEquals(1, asiakkaat.stream().filter(a -> a.id != null && a.id > 0 && a.nimi.equals(rnd.nimi) && a.puhelinnumero.equals("1") && a.katuosoite.equals("1") && a.postinumero.equals(1) && a.postitoimipaikka.equals("1")).count());

        poistaAsiakas(tmp, "Toinen");

        asiakkaat = dao.findAll();

        assertNotNull(asiakkaat);
        assertEquals(2, asiakkaat.size());

        assertEquals(1, asiakkaat.stream().filter(a -> a.id != null && a.id > 0 && a.nimi.equals("Asiakas") && a.puhelinnumero.equals("123-123123") && a.katuosoite.equals("123123") && a.postinumero.equals(13000) && a.postitoimipaikka.equals("Tampere")).count());
        assertEquals(1, asiakkaat.stream().filter(a -> a.id != null && a.id > 0 && a.nimi.equals(rnd.nimi) && a.puhelinnumero.equals("1") && a.katuosoite.equals("1") && a.postinumero.equals(1) && a.postitoimipaikka.equals("1")).count());

        tmp.delete();
    }

    @Test
    public void tilaustenLisaysJaListaus() throws ClassNotFoundException, SQLException {
        File tmp = new File("db", "tmp-" + UUID.randomUUID().toString());
        initDbs(tmp);

        Database db = new Database("jdbc:sqlite:" + tmp.getAbsolutePath());

        AsiakasDao aDao = new AsiakasDao(db);

        Asiakas a = aDao.saveOrUpdate(new Asiakas(null, UUID.randomUUID().toString().substring(0, 6), "1", "1", 1, "1"));

        Asiakas a2 = aDao.saveOrUpdate(new Asiakas(null, UUID.randomUUID().toString().substring(0, 6), "1", "1", 1, "1"));

        TilausDao tDao = new TilausDao(db, aDao);

        Tilaus tilaus = tDao.saveOrUpdate(new Tilaus(null, a, new Date(1), "mopo", Boolean.TRUE, Boolean.FALSE));

        List<Tilaus> tilaukset = tDao.findAll();

        assertEquals(1, tilaukset.size());

        assertEquals(1, tilaukset.stream().filter(t -> t.getId() != null && t.getId() > 0 && t.getAika().equals(tilaus.getAika()) && t.getKuljetustapa().equals("mopo")).count());
        assertEquals(1, tilaukset.stream().filter(t -> t.getAsiakas() != null && a.getId().equals(t.getAsiakas().getId())).count());

        Tilaus tilaus2 = tDao.saveOrUpdate(new Tilaus(null, a, new Date(20000), "fillari", Boolean.FALSE, Boolean.TRUE));

        tilaukset = tDao.findAll();
        assertEquals(2, tilaukset.size());

        assertEquals(1, tilaukset.stream().filter(t -> t.getId() != null && t.getId() > 0 && t.getAika().equals(tilaus.getAika()) && t.getKuljetustapa().equals("mopo")).count());
        assertEquals(1, tilaukset.stream().filter(t -> t.getId() != null && t.getId() > 0 && t.getAika().equals(tilaus2.getAika()) && t.getKuljetustapa().equals("fillari")).count());
        
        assertEquals(2, tilaukset.stream().filter(t -> t.getAsiakas() != null && a.getId().equals(t.getAsiakas().getId())).count());

        tmp.delete();
    }

    private void initDbs(File dbfile) throws SQLException {

        String createAsiakas = "CREATE TABLE Asiakas (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    nimi varchar(200),\n"
                + "    puhelinnumero varchar(20),\n"
                + "    katuosoite varcar(50),\n"
                + "    postinumero integer,\n"
                + "    postitoimipaikka varchar(20)\n"
                + ");";
        String createTilaus = "CREATE TABLE Tilaus (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    asiakas_id integer,\n"
                + "    aika date,\n"
                + "    kuljetustapa varchar(40),\n"
                + "    vastaanotettu boolean,\n"
                + "    toimitettu boolean,\n"
                + "    FOREIGN KEY (asiakas_id) REFERENCES Asiakas(id)\n"
                + ");";

        List<String> kyselyt = new ArrayList<>();
        kyselyt.add("DROP TABLE IF EXISTS Asiakas");
        kyselyt.add(createAsiakas);
        kyselyt.add("DROP TABLE IF EXISTS Tilaus");
        kyselyt.add(createTilaus);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbfile.getAbsolutePath())) {
            Statement stmt = conn.createStatement();

            for (String kysely : kyselyt) {
                stmt.executeUpdate(kysely);
            }
        }

    }

    public void poistaAsiakas(File dbfile, String nimi) throws SQLException {

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbfile.getAbsolutePath())) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM Asiakas WHERE nimi = '" + nimi + "'");
        }

    }
}
