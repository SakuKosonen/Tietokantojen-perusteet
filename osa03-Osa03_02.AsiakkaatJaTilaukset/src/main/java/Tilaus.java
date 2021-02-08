// älä muokkaa tätä tiedostoa

import java.sql.Date;

public class Tilaus {

    Integer id;
    Asiakas asiakas;
    Date aika;
    String kuljetustapa;
    Boolean vastaanotettu;
    Boolean toimitettu;

    public Tilaus(Integer id, Asiakas asiakas, Date aika, String kuljetustapa, Boolean vastaanotettu, Boolean toimitettu) {
        this.id = id;
        this.asiakas = asiakas;
        this.aika = aika;
        this.kuljetustapa = kuljetustapa;
        this.vastaanotettu = vastaanotettu;
        this.toimitettu = toimitettu;
    }

    public Integer getId() {
        return id;
    }

    public Asiakas getAsiakas() {
        return asiakas;
    }

    public Date getAika() {
        return aika;
    }

    public String getKuljetustapa() {
        return kuljetustapa;
    }

    public Boolean getVastaanotettu() {
        return vastaanotettu;
    }

    public Boolean getToimitettu() {
        return toimitettu;
    }

}
