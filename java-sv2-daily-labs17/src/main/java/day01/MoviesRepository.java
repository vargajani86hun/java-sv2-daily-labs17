package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {
    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveMovie(String title, LocalDate releaseDate) {
        try (//language=sql
             Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO movies(title, release_date) VALUES (?, ?);"
             )) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(releaseDate));
            stmt.executeUpdate();
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
