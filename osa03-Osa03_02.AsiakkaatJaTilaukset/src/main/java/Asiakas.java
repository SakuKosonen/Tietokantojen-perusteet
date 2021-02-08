// älä muokkaa tätä tiedostoa

public class Asiakas {

    Integer id;
    String nimi;
    String puhelinnumero;
    String katuosoite;
    Integer postinumero;
    String postitoimipaikka;

    public Asiakas(Integer id, String nimi, String puhelinnumero, String katuosoite, Integer postinumero, String postitoimipaikka) {
        this.id = id;
        this.nimi = nimi;
        this.puhelinnumero = puhelinnumero;
        this.katuosoite = katuosoite;
        this.postinumero = postinumero;
        this.postitoimipaikka = postitoimipaikka;
    }

    // muita metodeja ym
    public Integer getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public String getKatuosoite() {
        return katuosoite;
    }

    public Integer getPostinumero() {
        return postinumero;
    }

    public String getPostitoimipaikka() {
        return postitoimipaikka;
    }

}
