package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public long saveActorAndGetGeneratedId(String name) {
        Optional<Actor> actor = findActorByName(name);
        if (actor.isPresent()) {
            return actor.get().getId();
        }
        try ( //language=sql
              Connection conn = dataSrc.getConnection();
              PreparedStatement pStmt = conn.prepareStatement(
                      "INSERT INTO actors (actor_name) VALUES (?);", PreparedStatement.RETURN_GENERATED_KEYS
              )) {
            pStmt.setString(1, name);
            pStmt.executeUpdate();
            try (ResultSet rs = pStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new IllegalStateException("Falied to insert actor " + name + ".");
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot update", sqle);
        }
    }

    public Optional<Actor> findActorByName(String name) {
        try ( //language=sql
              Connection conn = dataSrc.getConnection();
              PreparedStatement pStmt = conn.prepareStatement(
                      "SELECT * FROM actors WHERE actor_name = ?"
              )) {
            pStmt.setString(1, name);
            return processSelectStatement(pStmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannat run query", sqle);
        }
    }

    private Optional<Actor> processSelectStatement(PreparedStatement pStmt) throws SQLException {
        try (ResultSet rs = pStmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(new Actor(rs.getLong("id"), rs.getString("actor_name")));
            } else {
                return Optional.empty();
            }
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
