package day01;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

public class MoviesRatingsService {
    private MoviesRepository moviesRepo;
    private RatingsRepository ratingsRepo;

    public MoviesRatingsService(MoviesRepository moviesRepo, RatingsRepository ratingsRepo) {
        this.moviesRepo = moviesRepo;
        this.ratingsRepo = ratingsRepo;
    }

    public void addRatings(String title, LocalDate releaseDate, Integer... ratings) {
        Optional<Movie> actual = moviesRepo.findMovie(title, releaseDate);
        if (actual.isPresent()) {
            long id = actual.get().getId();
            ratingsRepo.insertRatings(id, Arrays.asList(ratings));
            moviesRepo.updateMovieRating(id);
        } else {
            throw new IllegalArgumentException("Cannot find movie " + title + ", released at " + releaseDate);
        }
    }
}
