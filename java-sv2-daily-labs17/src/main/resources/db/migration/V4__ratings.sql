CREATE TABLE ratings (id BIGINT AUTO_INCREMENT, movie_id BIGINT, rating INT(1),
CONSTRAINT PK_ratings PRIMARY KEY (id),
CONSTRAINT FK_movies_in_ratings FOREIGN KEY (movie_id) REFERENCES movies (id));