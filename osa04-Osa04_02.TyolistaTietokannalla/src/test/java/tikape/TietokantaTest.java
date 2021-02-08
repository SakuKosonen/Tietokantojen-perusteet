/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

@Points("04-02")
public class TietokantaTest {
    
    @Test
    public void tietokantaTiedostoOlemassa() {
        assertTrue("Tehtäväpohjan kansiossa \"db\" ei ole tiedostoa \"tasks.db\".", databaseFile().exists());
    }

    @Test
    public void onTauluTehtava() throws SQLException {
        haeTaulu("Tehtava");
    }

    @Test
    public void onSarakeNimi() throws SQLException {
        onSarake("Tehtava", "nimi", "varchar");
    }
    
    public void onSarake(String taulu, String sarakkeenNimi, String tyyppi) throws SQLException {
        Optional<Sarake> nimi = haeTaulunSarakkeet(taulu).stream().filter(s -> s.nimi.toLowerCase().equals(sarakkeenNimi)).findFirst();
        if (!nimi.isPresent()) {
            fail("Taulusta " + taulu + " puuttuu sarake nimeltä \"" + sarakkeenNimi + "\".");
        }

        if (!nimi.get().tyyppi.toLowerCase().trim().equals(tyyppi)) {
            fail("Taulun " + taulu + " sarakkeen tyypin piti olla " + tyyppi + ". Nyt tyyppi oli " + nimi.get().tyyppi);
        }
    }
    

    List<Sarake> haeTaulunSarakkeet(String taulu) throws SQLException {
        tietokantaTiedostoOlemassa();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + databaseFile().getAbsolutePath())) {
            ResultSet tulos = conn.prepareStatement("SELECT * FROM " + taulu).executeQuery();
            ResultSetMetaData meta = tulos.getMetaData();

            List<Sarake> sarakkeet = new ArrayList<>();
            for (int i = 0; i < meta.getColumnCount(); i++) {
                Sarake s = new Sarake();
                s.nimi = meta.getColumnName(i + 1);
                s.tyyppi = meta.getColumnTypeName(i + 1);

                sarakkeet.add(s);
            }

            return sarakkeet;
        }

    }

    public ResultSet haeTaulu(String taulu) throws SQLException {
        tietokantaTiedostoOlemassa();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + databaseFile().getAbsolutePath())) {
            ResultSet tulosrivit = conn.getMetaData().getTables(null, null, taulu, null);
            if (!tulosrivit.next()) {
                fail("Tehtäväpohjan kansiossa \"db\" oleva tiedosto \"tasks.db\" ei sisällä tietokantataulua " + taulu + ".");
            }

            return tulosrivit;
        }
    }

    static File databaseFile() {
        return new File("db", "tasks.db");
    }

    static class Sarake {

        String nimi;
        String tyyppi;
    }

}
