
public class YhteenvetokyselytOsa1 {

    public static void main(String[] args) {
        // Tässä tehtävässä ei tarvitse ohjelmoida Java-kielellä. 
        // Seuraa tehtävänannon ohjeita materiaalista.

    }

    public static String kysely1() {
        return "SELECT COUNT(*) FROM Album;";
    }

    public static String kysely2() {
        return "SELECT AVG(total) FROM Invoice;";
    }

    public static String kysely3() {
        return "SELECT COUNT(*) FROM Track INNER JOIN Genre ON Genre.GenreId = Track.GenreId WHERE Genre.Name LIKE 'Blues' OR Genre.Name LIKE 'Jazz' OR Genre.Name LIKE 'Metal';";
    }

}
