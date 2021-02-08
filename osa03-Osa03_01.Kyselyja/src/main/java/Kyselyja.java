
public class Kyselyja {

    public static void main(String[] args) {
        // Tässä tehtävässä ei tarvitse juurikaan ohjelmoida Java-kielellä. 
        // Seuraa tehtävänannon ohjeita materiaalista.

        System.out.println(kysely1());

    }

    public static String kysely1() {
        return "SELECT Distinct  Artist.Name FROM Artist, Album, Track, Genre WHERE Artist.ArtistId = Album.ArtistId AND Album.AlbumId = Track.AlbumId AND Track.GenreId = Genre.GenreId AND Genre.Name = 'Blues';";
    }

    public static String kysely2() {
        return "SELECT Distinct  PlayList.Name FROM Playlist, PlaylistTrack, Track, Album, Artist WHERE Playlist.PlaylistId = PlaylistTrack.PlaylistId AND PlaylistTrack.TrackId = Track.TrackId AND Track.AlbumId = Album.AlbumId AND Album.ArtistId = Artist.ArtistId AND Artist.Name = 'Eric Clapton';";
    }

    public static String kysely3() {
        return "SELECT Distinct Customer.Email FROM Customer, Invoice, InvoiceLine, Track, Genre WHERE Customer.CustomerId = Invoice.CustomerId AND Invoice.InvoiceId = InvoiceLine.InvoiceId AND InvoiceLine.TrackId = Track.TrackId AND Track.GenreId = Genre.GenreId AND Genre.Name = 'Jazz';";
    }

}
