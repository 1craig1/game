package com.gamereleasetracker.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gamereleasetracker.dto.api_dto.ApiGameDto;
import com.gamereleasetracker.dto.api_dto.ApiGameResponseDto;
import com.gamereleasetracker.model.Game;
import com.gamereleasetracker.model.GameStatus;
import com.gamereleasetracker.repository.GameRepository;

@Service
public class GameUpdateService{
    private final GameRepository gameRepository;
    private final RestTemplate restTemplate;

    // Number of games returned by each query
    private static int page_size = 40;

    // Get API URL and key from application.properties
    @Value("${rawg.api.url}")
    private String apiUrl;
    @Value("${rawg.api.key}")
    private String apiKey;

    public GameUpdateService(GameRepository gameRepository, RestTemplate restTemplate) {
        this.gameRepository = gameRepository;
        this.restTemplate = restTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        UpdateGames();
    }

    public void UpdateGames() {
        try {
            // Get today's date so we can search only upcoming games
            String today = LocalDate.now().toString();

            boolean lastPage = false;
            int page = 1;
            while (!lastPage) {
                // Create API call URL
                String uri = UriComponentsBuilder.fromUriString(apiUrl)
                    .path("/games")
                    .queryParam("key", apiKey)
                    .queryParam("dates", today + ",2099-12-31") // Search from today onwards
                    .queryParam("page", page++)
                    .queryParam("page_size", page_size)
                    .queryParam("ordering", "released") // Sort by release date
                    .toUriString();

                // Send GET request and store results as a ApiGameResponseDto
                ResponseEntity<ApiGameResponseDto> response = restTemplate.getForEntity(uri, ApiGameResponseDto.class);

                // Check if the GET request was successful
                if (!response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    System.err.println("API returned non-OK status: " + response.getStatusCode());
                    return;
                } 

                // Set next URL to next page
                lastPage = response.getBody().getNext() == null;

                // Iterate over each game returned to add/update them in database
                for (ApiGameDto dto : response.getBody().getResults()) {
                    // Ensure release date is not null
                    if (dto.getReleased() == null) {
                        System.err.println("Game " + dto.getName() + " had null release date");
                        continue;
                    }

                    // Check if game already exists in database
                    List<Game> matching = gameRepository.findByRawgGameSlug(dto.getSlug());
                    Game game;
                    if (matching.size() > 0) {
                        // Game already exists
                        game = matching.get(0);

                        // Skip game if it has not been updated since it was last updated in the database
                        if (game.getUpdatedAt().isAfter(dto.getUpdated().toInstant(ZoneOffset.UTC))) { // Assuming RAWG uses UTC
                            continue;
                        }
                        System.out.println("Updating " + dto.getName());
                    }
                    else {
                        // Game does not exists
                        game = new Game();
                        game.setRawgGameSlug(dto.getSlug());
                    }
                    // Set/Update fields
                    game.setTitle(dto.getName());
                    game.setReleaseDate(dto.getReleased());
                    game.setCoverImageUrl(dto.getBackgroundImage());
                    game.setAgeRating(dto.getEsrbRating());

                    // Save to game repository
                    gameRepository.save(game);
                }
            }
        } 
        catch (HttpClientErrorException e) { // 4xx response codes
            System.err.println("Client error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } 
        catch (HttpServerErrorException e) { // 5xx response codes
            System.err.println("Server error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } 
        catch (RestClientException e) { // Errors with parsing the API response
            e.printStackTrace();
            System.err.println("Error calling API: " + e.getMessage());
        }
    }
}