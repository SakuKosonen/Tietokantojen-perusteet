
import org.junit.Test;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Points("01-03")
public class TiedonMuokkaaminenTest {

    @Test
    public void tallennusJaLukeminenToimiiOikein() throws Throwable {
        List<Henkilo> henkilot = henkilot();

        String tiedosto = "tmp-" + UUID.randomUUID().toString();

        new Tallentaja().tallennaHenkilot(henkilot, tiedosto);

        List<Henkilo> luetut = new Lukija().lueHenkilot(tiedosto);

        assertTrue(luetut.size() == henkilot.size());

        for (Henkilo henkilo : henkilot) {
            long count = luetut.stream().filter(h -> henkilo.nimi.equals(h.nimi)
                    && henkilo.kayttajatunnus.equals(h.kayttajatunnus)
                    && henkilo.salasana.equals(h.salasana)
                    && henkilo.osoite.equals(h.osoite)
                    && henkilo.puhelinnumero.contains(h.puhelinnumero)).count();

            assertEquals(1L, count);
        }

        try {
            new File(tiedosto).delete();
        } catch (Throwable t) {

        }
    }

    @Test
    public void henkilonMuokkaaminenToimii1() throws Throwable {
        muokkaus();
    }

    @Test
    public void henkilonMuokkaaminenToimii2() throws Throwable {
        muokkaus();
    }

    public void muokkaus() throws Throwable {
        List<Henkilo> henkilot = henkilot();
        Collections.shuffle(henkilot);

        String tiedosto = "tmp-" + UUID.randomUUID().toString();

        new Tallentaja().tallennaHenkilot(henkilot, tiedosto);

        Henkilo muokattava = henkilot.get(new Random().nextInt(henkilot.size()));
        String tunnus = muokattava.kayttajatunnus;
        henkilot.remove(muokattava);

        muokattava = luoHenkilo("lc", "lois", "lois delcambre", "porttikatu 18", "001-503727777");
        henkilot.add(muokattava);

        new Tallentaja().korvaaHenkilo(tunnus, muokattava, tiedosto);

        List<Henkilo> luetut = new Lukija().lueHenkilot(tiedosto);

        assertTrue(luetut.size() == henkilot.size());

        for (Henkilo henkilo : henkilot) {
            long count = luetut.stream().filter(h -> henkilo.nimi.equals(h.nimi)
                    && henkilo.kayttajatunnus.equals(h.kayttajatunnus)
                    && henkilo.salasana.equals(h.salasana)
                    && henkilo.osoite.equals(h.osoite)
                    && henkilo.puhelinnumero.contains(h.puhelinnumero)).count();

            assertEquals(1L, count);
        }

        try {
            new File(tiedosto).delete();
        } catch (Throwable t) {

        }
    }

    public List<Henkilo> henkilot() {
        List<Henkilo> henkilot = new ArrayList<>();

        henkilot.add(luoHenkilo("ec", "codd", "Edgar F. Codd", "relaatiomalli 7", "123-1234567"));
        henkilot.add(luoHenkilo("mw", "monty", "Monty Widenius", "myslikatu 22", "3581313467"));
        henkilot.add(luoHenkilo("rb", "ray", "Raymond Boyce", "normaalitie 7", "000-1231231"));
        henkilot.add(luoHenkilo("eb", "elisa", "Elisa Bertrino", "turvakatu 2", "41-1413141"));
        henkilot.add(luoHenkilo("jw22", "prof", "Jennifer Widom", "kantatie 11", "001-123150234"));

        return henkilot;
    }

    public Henkilo luoHenkilo(String kayttajatunnus, String salasana, String nimi, String osoite, String puhelinnumero) {
        Henkilo h = new Henkilo();

        h.kayttajatunnus = kayttajatunnus;
        h.salasana = salasana;
        h.nimi = nimi;
        h.osoite = osoite;
        h.puhelinnumero = puhelinnumero;

        return h;
    }

}
