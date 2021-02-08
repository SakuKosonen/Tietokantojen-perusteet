
import java.util.ArrayList;
import java.util.List;

public class KurssisuoritustenLukeminen {

    public static void main(String[] args) throws Throwable {
        List<Opiskelija> opiskelijat = new ArrayList<>();
        List<Kurssisuoritus> kurssisuoritukset = new ArrayList<>();

        new Tallentaja().tallenna(opiskelijat, "opiskelija.data");
        new Tallentaja().tallenna(kurssisuoritukset, "kurssisuoritus.data");

        opiskelijat = new Lukija().lueOpiskelijat("opiskelija.data");

        // testaile toteuttamaasi toiminnallisuutta täällä
    }

}
