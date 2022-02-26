package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActorsMoviesRepository {

    private DataSource dataSource;

    public ActorsMoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertActorAndMovieId(long actorId, long movieId) {
        try(Connection conn = dataSource.getConnection();
            //language=sql
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO actors_movies(actor_id, movie_id) VALUES (?, ?);"
            )) {
            pstmt.setLong(1, actorId);
            pstmt.setLong(2, movieId);
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot insert", sqle);
        }
    }
}
