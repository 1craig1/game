package com.gamereleasetracker.service;

import com.gamereleasetracker.model.Game;
import com.gamereleasetracker.model.GameStatus;
import com.gamereleasetracker.dto.GameSummaryDto;
import com.gamereleasetracker.dto.GameDetailDto;
import com.gamereleasetracker.dto.PlatformDto;
import com.gamereleasetracker.repository.GameRepository;
import com.gamereleasetracker.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class GameService {
    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;
    @Autowired
    public GameService(GameRepository gameRepository, PlatformRepository platformRepository) {
        this.gameRepository = gameRepository;
        this.platformRepository = platformRepository;
    }

    /**
     * Retrieves a list of upcoming games.
     * This method is typically used to display a list of games that are scheduled for release in the future.
     *
     * @return A list of games that are scheduled for release after the current date.
     */
    public List<Game> getUpcomingGames() {
        return gameRepository.findByReleaseDateAfterOrderByReleaseDateAsc(LocalDate.now());
    }

    /**
     * Searches for games by title.
     * This method is typically used to implement search functionality in the application.
     *
     * @param searchTerm The term to search for in game titles.
     * @return A list of games that match the search term.
     */
    public List<Game> searchGames(String searchTerm) {
        return gameRepository.findByTitleContainingIgnoreCase(searchTerm);
    }
    
    /**
     * Retrieves a list of games by their release status.
     * This method is typically used to filter games by their current status.
     *
     * @param status The status to filter by (e.g., UPCOMING, RELEASED).
     * @return A list of games that match the specified status.
     */
    public List<Game> getGamesByStatus(GameStatus status) {
        return gameRepository.findByStatus(status);
    }

    /**
     * Retrieves a list of games available on a specific platform.
     * This method is typically used to filter games by platform.
     *
     * @param platformName The name of the platform (e.g., "PC").
     * @return A list of games that are available on the specified platform.
     */
    public List<Game> getGamesByPlatform(String platformName) {
        return gameRepository.findByPlatforms_Name(platformName);
    }

    /**
     * Finds games by title containing a specific string, ignoring case.
     * This method is typically used to implement search functionality in the application.
     *
     * @param title The title or part of the title to search for.
     * @return A list of games that match the search criteria.
     */
    public List<Game> findGamesByTitleContainingIgnoreCase(String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Retrieves a list of games that are scheduled for release after a given date.
     * This method is typically used to display upcoming games.
     *
     * @param currentDate The date to filter games by (e.g., current date).
     * @return A list of games that are scheduled for release after the specified date.
     */
    public List<Game> findGamesByReleaseDateAfter(LocalDate currentDate) {
        return gameRepository.findByReleaseDateAfterOrderByReleaseDateAsc(currentDate);
    }

    // /**
    //  * Retrieves a list of games by their genre.
    //  * This method is typically used to filter games by genre.
    //  *
    //  * @param genre The genre to filter by (e.g., "RPG", "Shooter").
    //  * @return A list of games that match the specified genre.
    //  */
    // public List<Game> getGamesByGenre(String genre) {
    //     return gameRepository.findByGenre(genre);
    // }

    /**
     * Retrieves a game by its ID.
     * This method is typically used to display game details.
     *
     * @param id The ID of the game to retrieve.
     * @return An Optional containing the game if found, or empty if not found.
     */
    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    /**
     * Retrieves all games from the repository.
     * This method is typically used to display a list of all games.
     *
     * @return A list of all games.
     */
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    // /**
    //  * Saves a new game or updates an existing one.
    //  * This method should be used with caution as it can modify the database.
    //  * Should be executable only by admins or authorized users
    //  * 
    //  * @param game The game to save or update.
    //  * @return The saved game entity.
    //  */
    // public Game saveGame(Game game) {
    //     return gameRepository.save(game);
    // }

    // public Game addGame(GameDetailDto detailDto, GameSummaryDto summaryDto, List<PlatformDto> platformDtos) {
    //     Game game = new Game();
    //     game.setTitle(detailDto.title());
    //     game.setDescription(detailDto.description());
    //     game.setReleaseDate(detailDto.releaseDate());
    //     game.setStatus(detailDto.status());
    //     game.setDeveloper(detailDto.developer());
    //     game.setPublisher(detailDto.publisher());
    //     game.setGenres(detailDto.genres());
    //     game.setCoverImageUrl(summaryDto.coverImageUrl());
    //     game.setRating(summaryDto.rating());
    //     List<String> platformNames = platformDtos.stream()
    //             .map(PlatformDto::name)
    //             .toList();
    //     List<com.gamereleasetracker.model.Platform> platforms = platformRepository.findByNameIn(platformNames);
    //     game.setPlatforms(platforms);
    //     return gameRepository.save(game);

    // }

    /**
     * Deletes a game by its ID.
     * This method should be used with caution as it will remove the game from the database.
     * Should be executable only by admins or authorized users
     *
     * @param id The ID of the game to delete.
     */
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    /**
     * Retrieves the GameRepository instance.
     * This method is typically used for testing purposes.
     *
     * @return The GameRepository instance.
     */
    public GameRepository getGameRepository() {
        return gameRepository;
    }
    
}
