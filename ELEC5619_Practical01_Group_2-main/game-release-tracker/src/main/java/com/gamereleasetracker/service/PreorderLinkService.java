package com.gamereleasetracker.service;
import com.gamereleasetracker.model.PreorderLink;
import com.gamereleasetracker.dto.PreorderLinkDto;
import com.gamereleasetracker.repository.PreorderLinkRepository;
import com.gamereleasetracker.repository.GameRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PreorderLinkService {
    private final PreorderLinkRepository preorderLinkRepository;
    private final GameRepository gameRepository;
    @Autowired
    public PreorderLinkService(PreorderLinkRepository preorderLinkRepository, GameRepository gameRepository) {
        this.preorderLinkRepository = preorderLinkRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * Retrieves a list of preorder links for a specific game.
     * This method is typically used to display preorder options for a game.
     *
     * @param gameId The ID of the game for which to retrieve preorder links.
     * @return A list of PreorderLink objects associated with the specified game.
     */
    public List<PreorderLink> getPreorderLinksByGameId(Long gameId) {
        return preorderLinkRepository.findByGameId(gameId);
    }

    /**
     * Adds a new preorder link.
     * This method is typically used to create a new preorder option for a game.
     * Should only be executable by admins or authorized users.
     *
     * @param preorderLink The PreorderLink object to be added.
     * @return The saved PreorderLink object.
     */
    public PreorderLink addPreorderLink(PreorderLinkDto preorderLinkDto) {
        PreorderLink preorderLink = new PreorderLink();
        preorderLink.setGame(gameRepository.findById(preorderLinkDto.gameId())
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + preorderLinkDto.gameId())));
        return preorderLinkRepository.save(preorderLink);
    }

    /**
     * Deletes a preorder link.
     * This method is typically used to remove a preorder option from the database.
     * Should only be executable by admins or authorized users.
     *
     * @param preorderLink The PreorderLink object to be deleted.
     */
    public void deletePreorderLink(PreorderLinkDto preorderLinkDto) {
        PreorderLink preorderLink = preorderLinkRepository.findById(preorderLinkDto.id())
                .orElseThrow(() -> new RuntimeException("Preorder link not found with id: " + preorderLinkDto.id()));
        preorderLinkRepository.delete(preorderLink);
    }

    /**
     * Updates an existing preorder link.
     * This method is typically used to modify the details of a preorder option.
     * Should only be executable by admins or authorized users.
     *
     * @param preorderLink The PreorderLink object with updated information.
     * @return The updated PreorderLink object.
     */
    public PreorderLink updatePreorderLinkUrl(PreorderLinkDto preorderLinkDto) {
        PreorderLink preorderLink = preorderLinkRepository.findById(preorderLinkDto.id())
                .orElseThrow(() -> new RuntimeException("Preorder link not found with id: " + preorderLinkDto.id()));
        if (preorderLinkDto.url() != null) {
            preorderLink.setUrl(preorderLinkDto.url());
        }
        if (preorderLinkDto.gameId() != null) {
            preorderLink.setGame(gameRepository.findById(preorderLinkDto.gameId())
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + preorderLinkDto.gameId())));
        }
        if (preorderLinkDto.storeName() != null) {
            preorderLink.setStoreName(preorderLinkDto.storeName());
        }
        return preorderLinkRepository.save(preorderLink);
    }

    /**
     * Retrieves a preorder link by its ID.
     * This method is typically used to fetch a specific preorder link for further operations.
     *
     * @param id The ID of the preorder link to retrieve.
     * @return The PreorderLink object with the specified ID.
     */
    public PreorderLink getPreorderLinkById(Long id) {
        return preorderLinkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preorder link not found with id: " + id));
    }

    /**
     * Retrieves all preorder links from the database.
     * This method is typically used to display all available preorder options.
     *
     * @return A list of all PreorderLink objects.
     */
    public List<PreorderLink> getAllPreorderLinks() {
        return preorderLinkRepository.findAll();
    }

    /**
     * Retrieves the PreorderLinkRepository instance.
     * This method is typically used for testing purposes.
     *
     * @return The PreorderLinkRepository instance.
     */
    public PreorderLinkRepository getPreorderLinkRepository() {
        return preorderLinkRepository;
    }
}
