package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MariaDbDataSource mariaDbDSrc = new MariaDbDataSource();
        try {
            mariaDbDSrc.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            mariaDbDSrc.setUser("root");
            mariaDbDSrc.setPassword("Jani1234");

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase!", sqle);
        }

        Flyway flyway = Flyway.configure().dataSource(mariaDbDSrc).load();
        flyway.clean();
        flyway.migrate();

        ActorsRepository actorsRepo = new ActorsRepository(mariaDbDSrc);

        MoviesRepository moviesRepo = new MoviesRepository(mariaDbDSrc);

        ActorsMoviesRepository actorsMoviesRepo = new ActorsMoviesRepository(mariaDbDSrc);

        RatingsRepository ratingsRepo = new RatingsRepository(mariaDbDSrc);

        ActorsMoviesService service = new ActorsMoviesService(actorsRepo, moviesRepo, actorsMoviesRepo);

        MoviesRatingsService moviesRatingsService = new MoviesRatingsService(moviesRepo, ratingsRepo);

        service.insertMovieWithActors("Titanic", LocalDate.of(1997, 11, 17),
                List.of("Leonardo DiCaprio", "Kate Winslet"));
        service.insertMovieWithActors("Great Gatsby", LocalDate.of(2013, 5, 10),
                List.of("Leonardo DiCaprio", "Tobey Maguire"));
        service.insertMovieWithActors("Oscar", LocalDate.of(1967, 10, 11),
                List.of("Louis de Funés", "Claude Rich"));
        service.insertMovieWithActors("Oscar", LocalDate.of(1991, 4, 26),
                List.of("Sylvester Stallone", "Peter Riegert"));

        moviesRatingsService.addRatings("Titanic", LocalDate.of(1997, 11, 17), 5, 3, 2);
        moviesRatingsService.addRatings("Great Gatsby", LocalDate.of(2013, 5, 10), 5, 3, 2, 5, 4);
    }
}
