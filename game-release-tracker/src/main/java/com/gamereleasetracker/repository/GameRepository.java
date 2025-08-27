package com.gamereleasetracker.repository;

import com.gamereleasetracker.model.Game;
import com.gamereleasetracker.model.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/* Marks this interface as a Spring Data repository.
This tells Spring to create an implementation for it and to automatically
translate database errors into standard Spring exceptions. */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    /**
     * Finds all games scheduled for release after a given date.
     * This is a derived query method; Spring Data JPA automatically creates the
     * database query by parsing the method name:
     * - findBy...: The query's WHERE clause.
     * - ReleaseDateAfter: Finds records where the 'releaseDate' field is > the given date.
     * - OrderByReleaseDateAsc: Sorts the results by 'releaseDate' in ascending order.
     *
     * @param currentDate The current date and time
     * @return A list of matching games.
     */
    List<Game> findByReleaseDateAfterOrderByReleaseDateAsc(LocalDate currentDate);

    /**
     * Finds a list of games where the title contains the given search term, ignoring case.
     *
     * @param searchTerm The partial or full title to search for.
     * @return A list of matching games.
     */
    List<Game> findByTitleContainingIgnoreCase(String searchTerm);

    /**
     * Finds a list of games based on their release status.
     *
     * @param status The status to filter by (e.g., UPCOMING, RELEASED).
     * @return A list of matching games.
     */
    List<Game> findByStatus(GameStatus status);

    /**
     * Finds games available on a specific platform by the platform's name.
     * This query traverses the 'platforms' relationship.
     * @param name The name of the platform (e.g., "PC").
     * @return A list of matching games.
     */
    List<Game> findByPlatforms_Name(String name);

    /**
     * Finds games associated with a specific genre by the genre's name.
     * This query traverses the 'genres' many-to-many relationship.
     * @param name The name of the genre (e.g., "RPG").
     * @return A list of matching games.
     */
    List<Game> findByGenres_Name(String name);

    /**
     * Finds game by its RAWG Game Slug.
     * 
     * @param name The RAWG Game Slug of the game
     * @return A list containing matching game.
     */
    List<Game> findByRawgGameSlug(String rawgGameSlug);
}
