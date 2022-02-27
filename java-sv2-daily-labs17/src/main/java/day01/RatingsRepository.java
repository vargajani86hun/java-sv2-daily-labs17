package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RatingsRepository {
    private DataSource dataSource;

    public RatingsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRating(long movieId, int rating) {
        try (Connection conn = dataSource.getConnection();
            //language=sql
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO ratings (movie_id, raging) VALUES (?, ?);"
             )) {
            pstmt.setLong(1, movieId);
            pstmt.setInt(2, rating);
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to ratings!", sqle);
        }
    }

    public void insertRatings(long movieId, List<Integer> ratings) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            insertRatingsTo(movieId, ratings, conn);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to ratings!", sqle);
        }
    }

    private void insertRatingsTo(long movieId, List<Integer> ratings, Connection conn) throws SQLException {
        try (//language=sql
                PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO ratings (movie_id, rating) VALUES (?, ?);"
        )) {
            for(int i : ratings) {
                validateRating(i);
                stmt.setLong(1, movieId);
                stmt.setInt(2, i);
                stmt.executeUpdate();
            }
            conn.commit();
        } catch (IllegalArgumentException iae) {
            conn.rollback();
        }
    }

    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Invalid rating " + rating + "!");
        }
    }
}
