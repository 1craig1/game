package com.gamereleasetracker.service;
import com.gamereleasetracker.model.WishlistItem;
import com.gamereleasetracker.model.WishlistItemId;
import com.gamereleasetracker.repository.WishlistItemRepository;
import com.gamereleasetracker.repository.UserRepository;
import com.gamereleasetracker.repository.GameRepository;
import com.gamereleasetracker.dto.WishlistItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import com.gamereleasetracker.model.Game;
import com.gamereleasetracker.model.User;

import java.util.List;

public class WishlistItemService {
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Autowired
    public WishlistItemService(WishlistItemRepository wishlistItemRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * Adds a new WishlistItem to the user's wishlist.
     * Should only be executable by the user who owns the wishlist.
     *
     * @param wishlistItemDto The WishlistItemDto containing userId and gameId to be added.
     * @return The saved WishlistItem object.
     */
    public WishlistItem addWishlistItem(WishlistItemDto wishlistItemDto) {
        WishlistItem wishlistItem = new WishlistItem();
        WishlistItemId newId = new WishlistItemId(wishlistItemDto.userId(), wishlistItemDto.gameId());
        wishlistItem.setId(newId);
        wishlistItem.setUser(userRepository.findById(wishlistItemDto.userId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + wishlistItemDto.userId())));
        wishlistItem.setGame(gameRepository.findById(wishlistItemDto.gameId())
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + wishlistItemDto.gameId())));
        wishlistItem.setAddedAt(java.time.Instant.now());
        return wishlistItemRepository.save(wishlistItem);
    }

    /**
     * Removes a WishlistItem from the user's wishlist.
     * Should only be executable by the user who owns the wishlist item.
     *
     * @param wishlistItemDto The WishlistItemDto containing userId and gameId to identify the item to be removed.
     */
    public void removeWishlistItem(WishlistItemDto wishlistItemDto) {
        WishlistItem wishlistItem = wishlistItemRepository.findById(
                new WishlistItemId(wishlistItemDto.userId(), wishlistItemDto.gameId()))
                .orElseThrow(() -> new RuntimeException("Wishlist item not found for userId: "
                        + wishlistItemDto.userId() + " and gameId: " + wishlistItemDto.gameId()));
        wishlistItemRepository.delete(wishlistItem);
    }

    /**
     * Retrieves all wishlist items for a specific user.
     *
     * @param userId The ID of the user whose wishlist items are to be retrieved.
     * @return A list of Game objects in the user's wishlist.
     */
    public List<Game> getWishlistItemsByUserId(Long userId) {
        return wishlistItemRepository.findGamesByUserId(userId);
    }

    /**
     * Checks if a specific game is in the user's wishlist.
     *
     * @param userId The ID of the user.
     * @param gameId The ID of the game.
     * @return true if the game is in the wishlist, false otherwise.
     */
    public boolean isGameInWishlist(Long userId, Long gameId) {
        User user = new User();
        Game game = new Game();
        user.setId(userId);
        game.setId(gameId);
        return wishlistItemRepository.existsByUserAndGame(
                user, game);
    }
}
