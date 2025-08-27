package com.gamereleasetracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

/**
 * Represents a wishlist item in the application.
 * This class is a JPA entity that maps to the 'wishlist_items' table in the database.
 */
@Entity
@Table(name = "wishlist_items")
@Data
public class WishlistItem {

    /* Specifies that this entity uses a composite primary key.
    Unlike @Id for single-column keys, @EmbeddedId is used when the primary key
    is defined in a separate @Embeddable class (WishlistItemId in this case).
    The fields from the WishlistItemId class (userId, gameId) are mapped as the
    primary key columns for the 'wishlist_items' table. */
    @EmbeddedId
    private WishlistItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // Maps the userId field of the embedded ID
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gameId") // Maps the gameId field of the embedded ID
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(nullable = false, updatable = false)
    private Instant addedAt = Instant.now();

}