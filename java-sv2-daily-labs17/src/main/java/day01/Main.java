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
        flyway.migrate();

        ActorsRepository actorsRepo = new ActorsRepository(mariaDbDSrc);
        //actorsRepo.saveActor("John Wick");

        //System.out.println(actorsRepo.findActorsWithPrefix("J"));

        MoviesRepository moviesRepo = new MoviesRepository(mariaDbDSrc);
        moviesRepo.saveMovie("Ghost in the Shell", LocalDate.of(1995, 11, 18));

        List<Movie> movies = moviesRepo.findAllMovies();
        System.out.println(movies);
    }
}
