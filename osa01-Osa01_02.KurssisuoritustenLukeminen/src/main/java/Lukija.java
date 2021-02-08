
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lukija {

    public List<Opiskelija> lueOpiskelijat(String opiskelijatiedosto) throws Throwable {
        List<Opiskelija> opiskelijat = new ArrayList<>();

        Files.lines(Paths.get(opiskelijatiedosto)).forEach(rivi -> {
            String[] palat = rivi.split("\t");
            opiskelijat.add(new Opiskelija(palat[0], palat[1], Integer.parseInt(palat[2])));
        });

        return opiskelijat;
    }

    public List<Kurssisuoritus> lueKurssisuoritukset(String suoritustiedosto, String opiskelijatiedosto) throws Throwable {

        // lisää toiminnallisuus tänne
        List<Opiskelija> opiskelijat = new ArrayList<>();
        HashMap<String, List<String>> kurssit = new HashMap<>();
        List<Kurssisuoritus> kurssisuoritukset = new ArrayList<>();
        
        Files.lines(Paths.get(opiskelijatiedosto)).forEach(rivi -> {
            String[] palat = rivi.split("\t");
            opiskelijat.add(new Opiskelija(palat[0], palat[1], Integer.parseInt(palat[2])));

        });
        
        Files.lines(Paths.get(suoritustiedosto)).forEach(rivi -> {
            String[] palat = rivi.split("\t");
            if(!kurssit.keySet().contains(palat[0])) {
                kurssit.put(palat[0], new ArrayList<>());
            }
            
            kurssit.get(palat[0]).add(palat[1]);
        });
        
        for (Opiskelija x : opiskelijat) {
            for (String k : kurssit.get(x.opiskelijanumero)) {
                Kurssisuoritus jotain = new Kurssisuoritus();
                jotain.kurssi = k;
                jotain.opiskelija = x;
                kurssisuoritukset.add(jotain);
            }
            
        }
        

        return kurssisuoritukset;
    }
}
