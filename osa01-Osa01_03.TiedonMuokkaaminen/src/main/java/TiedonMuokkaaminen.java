
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TiedonMuokkaaminen {

    public static void main(String[] args) throws Throwable {
        Scanner lukija = new Scanner(System.in);

        List<Henkilo> henkilot = new ArrayList<>();

        System.out.println("Syötetään henkilöitä..");

        while (true) {
            System.out.println("Syötetäänkö? x lopettaa");
            String syotetaan = lukija.nextLine();
            if (syotetaan.equals("x")) {
                break;
            }

            System.out.println("Käyttäjätunnus?");
            String kayttajatunnus = lukija.nextLine();
            System.out.println("Salasana?");
            String salasana = lukija.nextLine();
            System.out.println("Nimi?");
            String nimi = lukija.nextLine();
            System.out.println("Osoite?");
            String osoite = lukija.nextLine();
            System.out.println("Puhelinnumero?");
            String puhelinnumero = lukija.nextLine();

            Henkilo h = new Henkilo();
            h.kayttajatunnus = kayttajatunnus;
            h.salasana = salasana;
            h.nimi = nimi;
            h.osoite = osoite;
            h.puhelinnumero = puhelinnumero;

            henkilot.add(h);
        }

        new Tallentaja().tallennaHenkilot(henkilot, "henkilot.data");

        System.out.println("");
        List<Henkilo> luetut = new Lukija().lueHenkilot("henkilot.data");

        for (Henkilo henkilo : luetut) {
            System.out.println(henkilo.kayttajatunnus);
            System.out.println(henkilo.salasana);
            System.out.println(henkilo.nimi);
            System.out.println(henkilo.osoite);
            System.out.println(henkilo.puhelinnumero);
            System.out.println("");
        }

        System.out.println("");
        System.out.println("Minkä käyttäjätunnuksen omaavan henkilön tietoja muokataan?");
        String tunnus = lukija.nextLine();

        System.out.println("Uusi käyttäjätunnus?");
        String kayttajatunnus = lukija.nextLine();
        System.out.println("Uusi salasana?");
        String salasana = lukija.nextLine();
        System.out.println("Uusi nimi?");
        String nimi = lukija.nextLine();
        System.out.println("Uusi osoite?");
        String osoite = lukija.nextLine();
        System.out.println("Uusi puhelinnumero?");
        String puhelinnumero = lukija.nextLine();

        Henkilo h = new Henkilo();
        h.kayttajatunnus = kayttajatunnus;
        h.salasana = salasana;
        h.nimi = nimi;
        h.osoite = osoite;
        h.puhelinnumero = puhelinnumero;

        new Tallentaja().korvaaHenkilo(tunnus, h, "henkilot.data");
    }

}
