
import java.util.ArrayList;
import java.util.List;

public class Opiskelija {

    List<Kurssisuoritus> kurssisuoritukset;
    String opiskelijanumero;
    String nimi;
    int syntymavuosi;

    public Opiskelija(String opiskelijanumero, String nimi, int syntymavuosi) {
        this.opiskelijanumero = opiskelijanumero;
        this.nimi = nimi;
        this.kurssisuoritukset = new ArrayList<>();
        this.syntymavuosi = syntymavuosi;
    }

    @Override
    public String toString() {
        return this.opiskelijanumero + "\t" + this.nimi + "\t" + this.syntymavuosi;
    }
}
