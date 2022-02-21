package day01;

import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.SQLException;

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

        ActorsRepository actorsRepo = new ActorsRepository(mariaDbDSrc);
        //actorsRepo.saveActor("John Wick");

        System.out.println(actorsRepo.findActorsWithPrefix("J"));
    }
}
