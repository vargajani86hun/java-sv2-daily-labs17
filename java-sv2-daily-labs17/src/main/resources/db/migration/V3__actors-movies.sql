CREATE TABLE actors_movies (id BIGINT AUTO_INCREMENT, actor_id BIGINT, movie_id BIGINT,
CONSTRAINT PK_actors_movies PRIMARY KEY (id),
CONSTRAINT FK_actors FOREIGN KEY (actor_id) REFERENCES actors(id),
CONSTRAINT FK_movies FOREIGN KEY (movie_id) REFERENCES movies(id));