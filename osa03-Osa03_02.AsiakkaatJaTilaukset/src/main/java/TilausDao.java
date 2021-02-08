
import java.util.*;
import java.sql.*;

public class TilausDao implements Dao<Tilaus, Integer> {

    private Database database;
    private Dao<Asiakas, Integer> asiakasDao;

    public TilausDao(Database database, Dao<Asiakas, Integer> asiakasDao) {
        this.database = database;
        this.asiakasDao = asiakasDao;
    }

    @Override
    public Tilaus findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Tilaus WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Asiakas asiakas = asiakasDao.findOne(rs.getInt("asiakas_id"));

        Tilaus t = new Tilaus(key, asiakas,
                rs.getDate("aika"), rs.getString("kuljetustapa"),
                rs.getBoolean("vastaanotettu"), rs.getBoolean("toimitettu"));

        rs.close();
        stmt.close();
        connection.close();

        return t;
    }

    // hakee kaikki tilaukset tietokannasta ja palauttaa ne listalla
    // jokaiseen tilaukseen tulee olla kytkettynä myös asiakas-olio
    @Override
    public List<Tilaus> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Tilaus");

        ResultSet rs = stmt.executeQuery();

        ArrayList<Tilaus> Lista = new ArrayList<>();

        while (rs.next()) {

            Tilaus t = new Tilaus(rs.getInt("id"), asiakasDao.findOne(rs.getInt("Asiakas_id")),
                    rs.getDate("aika"), rs.getString("kuljetustapa"),
                    rs.getBoolean("vastaanotettu"), rs.getBoolean("toimitettu"));
            Lista.add(t);
        }

        // ei toteutettu
        return Lista;

    }

    // Tallentaa tai päivittää tilauksen. Jos tilauksella ei ole asetettuna 
    // pääavainta, tilaus tallennetaan tietokantaan. Jos pääavain on asetettu, 
    // vanhan tilauksen tiedot tulee päivittää
    @Override
    public Tilaus saveOrUpdate(Tilaus tilaus) throws SQLException {
        if (tilaus.getId() == null) {
            return save(tilaus);
        } else {
            return update(tilaus);
        }
    }

    // Poistaa tilauksen tietokannasta.
    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Tilaus WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    private Tilaus save(Tilaus tilaus) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Tilaus"
                + " (aika, kuljetustapa, vastaanotettu, toimitettu, asiakas_id)"
                + " VALUES (?, ?, ?, ?, ?)");
        stmt.setDate(1, tilaus.getAika());
        stmt.setString(2, tilaus.getKuljetustapa());
        stmt.setBoolean(3, tilaus.getVastaanotettu());
        stmt.setBoolean(4, tilaus.getToimitettu());
        stmt.setInt(5, tilaus.getAsiakas().getId());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM Tilaus"
                + " WHERE aika = ? AND asiakas_id = ?");
        stmt.setDate(1, tilaus.getAika());
        stmt.setInt(2, tilaus.getAsiakas().getId());

        ResultSet rs = stmt.executeQuery();
        rs.next(); // vain 1 tulos

        Tilaus t = new Tilaus(rs.getInt("id"),
                tilaus.getAsiakas(),
                rs.getDate("aika"),
                rs.getString("kuljetustapa"),
                rs.getBoolean("vastaanotettu"),
                rs.getBoolean("toimitettu"));

        stmt.close();
        rs.close();

        conn.close();

        return t;
    }

    private Tilaus update(Tilaus tilaus) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Tilaus SET"
                + " aika = ?, kuljetustapa = ?, vastaanotettu = ?, toimitettu = ?, asiakas_id = ? WHERE id = ?");
        stmt.setDate(1, tilaus.getAika());
        stmt.setString(2, tilaus.getKuljetustapa());
        stmt.setBoolean(3, tilaus.getVastaanotettu());
        stmt.setBoolean(4, tilaus.getToimitettu());
        stmt.setInt(5, tilaus.getAsiakas().getId());

        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return tilaus;
    }
}
