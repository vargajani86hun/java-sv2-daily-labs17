package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActorsRepository {
    private DataSource dataSrc;

    public ActorsRepository(DataSource dataSrc) {
        this.dataSrc = dataSrc;
    }

    public void saveActor(String name) {
        try ( //language=sql
             Connection conn = dataSrc.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(
                     "INSERT INTO actors (actor_name) VALUES (?);"
             )) {
            pStmt.setString(1, name);
            pStmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot update", sqle);
        }
    }

    public List<String> findActorsWithPrefix(String prefix) {
        try ( //language=sql
                Connection conn = dataSrc.getConnection();
                PreparedStatement pStmt = conn.prepareStatement(
                        "SELECT actor_name FROM actors WHERE actor_name LIKE ?"
                )) {
            pStmt.setString(1, prefix + "%");
            return getColumnsValuesFrom(pStmt, "actor_name");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannat run query", sqle);
        }
    }

    private List<String> getColumnsValuesFrom(PreparedStatement stmt, String columnLabel) throws SQLException{
        List<String> results = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String actorName = rs.getString(columnLabel);
                results.add(actorName);
            }
        }
        return results;
    }
}
