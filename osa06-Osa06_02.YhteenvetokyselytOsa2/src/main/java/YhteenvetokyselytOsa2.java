
public class YhteenvetokyselytOsa2 {

    public static void main(String[] args) {
        // Tässä tehtävässä ei tarvitse ohjelmoida Java-kielellä. 
        // Seuraa tehtävänannon ohjeita materiaalista.

    }

    public static String kysely1() {
        return "SELECT Genre.Name AS genre, COUNT(*) AS kappaleita FROM Genre, Track WHERE Genre.GenreId = Track.GenreId GROUP BY Genre.Name;";
    }

    public static String kysely2() {
        return " SELECT Genre.Name AS genre, COUNT(*) AS ostettuja FROM Genre, Track, InvoiceLine, Invoice WHERE Genre.GenreId = Track.GenreId AND Track.TrackId = InvoiceLine.TrackId AND InvoiceLine.InvoiceId = Invoice.InvoiceId GROUP BY Genre.Name;";
    }

    public static String kysely3() {
        return "SELECT Artist.Name AS artisti, COUNT(*) AS levyt FROM Artist, Album WHERE Artist.ArtistId = Album.ArtistId GROUP BY Artist.Name;";
    }

}
