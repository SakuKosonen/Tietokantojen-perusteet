
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class Lukija {

    public List<Henkilo> lueHenkilot(String tiedosto) throws Throwable {
        RandomAccessFile raf = new RandomAccessFile(tiedosto, "r");

        List<Henkilo> henkilot = new ArrayList<>();

        raf.seek(0);
        byte[] kayttajatunnus = new byte[8];
        byte[] salasana = new byte[8];
        byte[] nimi = new byte[30];
        byte[] osoite = new byte[30];
        byte[] puhelinnumero = new byte[15];

        while (raf.getFilePointer() < raf.length()) {
            Henkilo h = new Henkilo();

            raf.read(kayttajatunnus);
            raf.read(salasana);
            raf.read(nimi);
            raf.read(osoite);
            raf.read(puhelinnumero);

            h.kayttajatunnus = new String(kayttajatunnus).trim();
            h.salasana = new String(salasana).trim();
            h.nimi = new String(nimi).trim();
            h.osoite = new String(osoite).trim();
            h.puhelinnumero = new String(puhelinnumero).trim();

            henkilot.add(h);
        }

        return henkilot;
    }
}
