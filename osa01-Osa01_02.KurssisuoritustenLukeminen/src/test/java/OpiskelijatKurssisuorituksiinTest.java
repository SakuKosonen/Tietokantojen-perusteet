
import org.junit.Test;
import org.junit.Rule;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Points("01-02")
public class OpiskelijatKurssisuorituksiinTest {

    @Rule
    public MockStdio io = new MockStdio();

    @Test
    public void jokainenKurssisuoritusEsiintyyDatassaVainKerran() throws Throwable {
        Stats testidata = luoTestidata();

        List<Kurssisuoritus> suoritukset = new Lukija().lueKurssisuoritukset("ks-" + testidata.tiedosto, "op-" + testidata.tiedosto);

        assertEquals(testidata.suoritukset.size(), suoritukset.size());

        poistaTestidata(testidata);
    }

    @Test
    public void kurssisuoritustenNimetLuettuOikein() throws Throwable {
        Stats testidata = luoTestidata();

        List<Kurssisuoritus> suoritukset = new Lukija().lueKurssisuoritukset("ks-" + testidata.tiedosto, "op-" + testidata.tiedosto);
        List<String> kurssit = suoritukset.stream().map(s -> s.kurssi).collect(Collectors.toList());

        testidata.suoritukset.forEach(s -> kurssit.remove(s.kurssi));

        assertTrue("Kun kurssisuoritukset luettiin, kurssien nimien lukeminen epäonnistui. Nyt " + kurssit.size() + " nimi / nimeä jäi lukematta.", kurssit.size() == 0);

        poistaTestidata(testidata);
    }

    @Test
    public void jokainenOpiskelijaEsiintyyDatassaVainKerran() throws Throwable {
        Stats testidata = luoTestidata();

        List<Kurssisuoritus> suoritukset = new Lukija().lueKurssisuoritukset("ks-" + testidata.tiedosto, "op-" + testidata.tiedosto);
        List<Opiskelija> opiskelijat = suoritukset.stream().map(s -> s.opiskelija).distinct().collect(Collectors.toList());

        assertEquals(testidata.opiskelijat.size(), opiskelijat.size());

        poistaTestidata(testidata);
    }

    @Test
    public void opiskelijoidenTiedotLuettuOikein() throws Throwable {
        Stats testidata = luoTestidata();

        List<Kurssisuoritus> suoritukset = new Lukija().lueKurssisuoritukset("ks-" + testidata.tiedosto, "op-" + testidata.tiedosto);
        List<Opiskelija> opiskelijat = suoritukset.stream().map(s -> s.opiskelija).distinct().collect(Collectors.toList());

        for (Opiskelija opiskelija : testidata.opiskelijat) {
            assertEquals("Varmista, että jokaiselta opiskelijalta luetaan nimi, opiskelijanumero ja syntymävuosi. Varmista lisäksi, että tiedot asetetaan oikeisiin muuttujiin.", 1, opiskelijat.stream().filter(o -> opiskelija.nimi.equals(o.nimi) && opiskelija.opiskelijanumero.equals(o.opiskelijanumero) && opiskelija.syntymavuosi == o.syntymavuosi).count());
        }

        poistaTestidata(testidata);
    }

    private Stats luoTestidata() throws Throwable {
        Stats stats = new Stats();

        new Tallentaja().tallenna(stats.opiskelijat, "op-" + stats.tiedosto);
        new Tallentaja().tallenna(stats.suoritukset, "ks-" + stats.tiedosto);

        return stats;
    }

    private void poistaTestidata(Stats stats) {
        try {
            new File("op-" + stats.tiedosto).delete();
        } catch (Throwable t) {

        }
        try {
            new File("ks-" + stats.tiedosto).delete();
        } catch (Throwable t) {

        }
    }

    public static class Stats {

        String tiedosto;
        List<Kurssisuoritus> suoritukset = new ArrayList<>();
        List<Opiskelija> opiskelijat = new ArrayList<>();

        public Stats() {
            this.tiedosto = "test-" + UUID.randomUUID().toString();
            int suorituksia = 20 + new Random().nextInt(20);
            int opiskelijoita = 5 + new Random().nextInt(5);

            for (int i = 0; i < opiskelijoita; i++) {
                Opiskelija o = new Opiskelija("1" + i, UUID.randomUUID().toString(), 1999 + i);

                opiskelijat.add(o);
            }

            for (int i = 0; i < suorituksia; i++) {
                Kurssisuoritus s = new Kurssisuoritus();
                s.kurssi = UUID.randomUUID().toString();
                Collections.shuffle(opiskelijat);
                s.opiskelija = opiskelijat.get(0);

                suoritukset.add(s);
            }

            for (Kurssisuoritus kurssisuoritus : suoritukset) {
                if (kurssisuoritus.opiskelija.kurssisuoritukset == null) {
                    kurssisuoritus.opiskelija.kurssisuoritukset = new ArrayList<>();
                }

                kurssisuoritus.opiskelija.kurssisuoritukset.add(kurssisuoritus);
            }
        }

    }
}
