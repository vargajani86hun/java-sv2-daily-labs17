package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoviesRepository {
    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveMovie(String title, LocalDate releaseDate) {
        Optional<Movie> movie = findMovie(title, releaseDate);
        if (movie.isPresent()) {
            return movie.get().getId();
        }
        try (//language=sql
             Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO movies(title, release_date) VALUES (?, ?);", PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(releaseDate));
            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new IllegalStateException("Insert failed to movies!");
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Unable to connect", sqle);
        }
    }

    public List<Movie> findAllMovies() {

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             //language=sql
             ResultSet rs = stmt.executeQuery(
                     "SELECT * FROM movies;"
             )) {

            return readMoviesFromResultSet(rs);

        } catch (SQLException sqle) {
            throw new IllegalStateException("Unable to read db", sqle);
        }

    }

    public Optional<Movie> findMovie(String title, LocalDate releaseDate) {
        try (Connection conn = dataSource.getConnection();
             //language=sql
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM movies WHERE title = ? AND release_date = ?;"
             )) {
            pstmt.setString(1, title);
            pstmt.setDate(2, Date.valueOf(releaseDate));
            return processQuery(pstmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Unable to read movies table!");
        }
    }

    private Optional<Movie> processQuery(PreparedStatement pstmt) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(new Movie(rs.getLong("id"),
                        rs.getString("title"),
                        rs.getDate("release_date").toLocalDate()));
            } else {
                return Optional.empty();
            }
        }
    }

    private List<Movie> readMoviesFromResultSet(ResultSet rs) throws SQLException{
        List<Movie> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new Movie(rs.getLong("id"),
                    rs.getString("title"),
                    rs.getDate("release_date").toLocalDate()));
        }
        return results;
    }
}
