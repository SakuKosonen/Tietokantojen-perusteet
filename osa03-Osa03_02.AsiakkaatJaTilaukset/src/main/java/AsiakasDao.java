
import java.util.*;
import java.sql.*;

public class AsiakasDao implements Dao<Asiakas, Integer> {

    private Database database;

    public AsiakasDao(Database database) {
        this.database = database;
    }

    @Override
    public Asiakas findOne(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Asiakas WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Asiakas a = new Asiakas(rs.getInt("id"), rs.getString("nimi"),
                rs.getString("puhelinnumero"), rs.getString("katuosoite"),
                rs.getInt("postinumero"), rs.getString("postitoimipaikka"));

        stmt.close();
        rs.close();

        conn.close();

        return a;
    }

    // hakee kaikki asiakkaat tietokannasta ja palauttaa ne listalla
    @Override
    public List<Asiakas> findAll() throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Asiakas");

        ResultSet rs = stmt.executeQuery();

        ArrayList<Asiakas> Lista = new ArrayList<>();

        while (rs.next()) {
            Asiakas c = new Asiakas(rs.getInt("id"), rs.getString("nimi"),
                    rs.getString("puhelinnumero"), rs.getString("katuosoite"),
                    rs.getInt("postinumero"), rs.getString("postitoimipaikka"));
            Lista.add(c);
        }

        return Lista;
    }

    @Override
    public Asiakas saveOrUpdate(Asiakas object) throws SQLException {
        // jos asiakkaalla ei ole pääavainta, oletetaan, että asiakasta
        // ei ole vielä tallennettu tietokantaan ja tallennetaan asiakas
        if (object.id == null) {
            return save(object);
        } else {
            // muulloin päivitetään asiakas
            return update(object);
        }
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Asiakas WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    private Asiakas save(Asiakas asiakas) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Asiakas"
                + " (nimi, puhelinnumero, katuosoite, postinumero, postitoimipaikka)"
                + " VALUES (?, ?, ?, ?, ?)");
        stmt.setString(1, asiakas.getNimi());
        stmt.setString(2, asiakas.getPuhelinnumero());
        stmt.setString(3, asiakas.getKatuosoite());
        stmt.setInt(4, asiakas.getPostinumero());
        stmt.setString(5, asiakas.getPostitoimipaikka());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM Asiakas"
                + " WHERE nimi = ? AND puhelinnumero = ?");
        stmt.setString(1, asiakas.getNimi());
        stmt.setString(2, asiakas.getPuhelinnumero());

        ResultSet rs = stmt.executeQuery();
        rs.next(); // vain 1 tulos

        Asiakas a = new Asiakas(rs.getInt("id"), rs.getString("nimi"),
                rs.getString("puhelinnumero"), rs.getString("katuosoite"),
                rs.getInt("postinumero"), rs.getString("postitoimipaikka"));

        stmt.close();
        rs.close();

        conn.close();

        return a;
    }

    private Asiakas update(Asiakas asiakas) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Asiakas SET"
                + " nimi = ?, puhelinnumero = ?, katuosoite = ?, postinumero = ?, postitoimipaikka = ? WHERE id = ?");
        stmt.setString(1, asiakas.getNimi());
        stmt.setString(2, asiakas.getPuhelinnumero());
        stmt.setString(3, asiakas.getKatuosoite());
        stmt.setInt(4, asiakas.getPostinumero());
        stmt.setString(5, asiakas.getPostitoimipaikka());
        stmt.setInt(6, asiakas.getId());

        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return asiakas;
    }
}
