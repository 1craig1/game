package com.gamereleasetracker.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a preorder link in the application.
 * This class is a JPA entity that maps to the 'preorder_links' table in the database.
 */

@Entity
@Table(name = "preorder_links")
@Data
public class PreorderLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String storeName;

    @Column(nullable = false)
    private String url;

    // There can be many PreorderLinks for one Game.
    @ManyToOne(fetch = FetchType.LAZY)
    // Specifies the foreign key column in the 'preorder_links' table that links to the 'games' table.
    /* 'name = "game_id"' sets the name of this foreign key column.
    'nullable = false' makes this a required relationship, ensuring that every
    preorder link must be associated with a game. */
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

}