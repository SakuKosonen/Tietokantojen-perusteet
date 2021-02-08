
import org.junit.Test;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Points("02-01")
public class KappaleetTest {

    @Test
    public void tiedostoLevyDbOlemassa() {
        assertTrue("Tehtäväpohjan kansiossa \"db\" ei ole tiedostoa \"levy.db\".", databaseFile().exists());
    }

    @Test
    public void onTauluKappale() throws SQLException {
        haeTauluKappale();
    }

    @Test
    public void onSarakeNimi() throws SQLException {
        onSarake("nimi", "varchar");
    }

    @Test
    public void onSarakeArtisti() throws SQLException {
        onSarake("artisti", "varchar");
    }

    @Test
    public void onSarakeLevytysvuosi() throws SQLException {
        onSarake("levytysvuosi", "integer");
    }

    @Test
    public void onSarakePituus() throws SQLException {
        onSarake("pituus", "integer");
    }

    @Test
    public void onVainToivotutSarakkeet() throws SQLException {
        int sarakkeita = haeKappaleenSarakkeet().size();
        assertTrue("Taulussa Kappale tulee olla täsmälleen neljä saraketta. Nyt sarakkeita oli " + sarakkeita + ".", sarakkeita == 4);
    }
    
    @Test
    public void onKolmeKappaletta() throws SQLException {
        List<Kappale> kappaleet = haeKappaleet();
        
        assertTrue("Tietokantataulussa Kappale pitäisi olla 3 kappaletta. Nyt niitä oli " + kappaleet.size() + ".", kappaleet.size() == 3);
        
    }

    @Test
    public void onToivottuSisalto() throws SQLException {
        onVainToivotutSarakkeet();

        List<Kappale> kappaleet = haeKappaleet();
        
        List<Kappale> odotetut = new ArrayList<>();
        odotetut.add(new Kappale("Capito Tutto", "Kummeli", 1994, 124));
        odotetut.add(new Kappale("Kanada", "Kummeli", 1994, 119));
        odotetut.add(new Kappale("Tango Vibrato", "Kummeli", 1994, 117));

        for (Kappale odotettu : odotetut) {
            if(kappaleet.contains(odotettu)) {
                continue;
            }
            
            fail("Tietokannasta ei löytynyt kappaletta " + odotettu.nimi + " (" + odotettu.artisti + "), pituus " + odotettu.pituus + ", levytysvuosi " + odotettu.levytysvuosi + ".");
        }
        
    }

    public void onSarake(String sarakkeenNimi, String tyyppi) throws SQLException {
        Optional<Sarake> nimi = haeKappaleenSarakkeet().stream().filter(s -> s.nimi.toLowerCase().equals(sarakkeenNimi)).findFirst();
        if (!nimi.isPresent()) {
            fail("Taulusta Kappale puuttuu sarake nimeltä \"" + sarakkeenNimi + "\".");
        }

        if (!nimi.get().tyyppi.toLowerCase().trim().equals(tyyppi)) {
            fail("Taulun Kappale sarakkeen tyypin piti olla " + tyyppi + ". Nyt tyyppi oli " + nimi.get().tyyppi);
        }
    }
    
    List<Kappale> haeKappaleet() throws SQLException {
        tiedostoLevyDbOlemassa();
        
        List<Kappale> kappaleet = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + databaseFile().getAbsolutePath())) {
            ResultSet tulos = conn.prepareStatement("SELECT * FROM Kappale").executeQuery();
            while (tulos.next()) {

                Kappale k = new Kappale();

                k.nimi = tulos.getString("nimi");
                k.artisti = tulos.getString("artisti");
                k.levytysvuosi = tulos.getInt("levytysvuosi");
                k.pituus = tulos.getInt("pituus");

                kappaleet.add(k);
            }
        }

        return kappaleet;
    }

    List<Sarake> haeKappaleenSarakkeet() throws SQLException {
        tiedostoLevyDbOlemassa();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + databaseFile().getAbsolutePath())) {
            ResultSet tulos = conn.prepareStatement("SELECT * FROM Kappale").executeQuery();
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

    public ResultSet haeTauluKappale() throws SQLException {
        tiedostoLevyDbOlemassa();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + databaseFile().getAbsolutePath())) {
            ResultSet tulosrivit = conn.getMetaData().getTables(null, null, "Kappale", null);
            if (!tulosrivit.next()) {
                fail("Tehtäväpohjan kansiossa \"db\" oleva tiedosto \"levy.db\" ei sisällä tietokantataulua Kappale.");
            }

            return tulosrivit;
        }
    }

    static File databaseFile() {
        return new File("db", "levy.db");
    }

    static class Sarake {

        String nimi;
        String tyyppi;
    }

    static class Kappale {

        String nimi;
        String artisti;
        Integer levytysvuosi;
        Integer pituus;

        public Kappale() {
        }

        public Kappale(String nimi, String artisti, Integer levytysvuosi, Integer pituus) {
            this.nimi = nimi;
            this.artisti = artisti;
            this.levytysvuosi = levytysvuosi;
            this.pituus = pituus;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Kappale other = (Kappale) obj;
            if (!Objects.equals(this.nimi, other.nimi)) {
                return false;
            }
            if (!Objects.equals(this.artisti, other.artisti)) {
                return false;
            }
            if (!Objects.equals(this.levytysvuosi, other.levytysvuosi)) {
                return false;
            }
            if (!Objects.equals(this.pituus, other.pituus)) {
                return false;
            }
            return true;
        }

    }
}
