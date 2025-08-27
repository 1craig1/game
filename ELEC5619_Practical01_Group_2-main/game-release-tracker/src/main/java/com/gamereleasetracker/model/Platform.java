package com.gamereleasetracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a gaming platform (e.g., PC, PlayStation 5).
 */
@Entity
@Table(name = "platforms")
@Data
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    // This is the "inverse" side of the relationship.
    // 'mappedBy = "platforms"' tells JPA that the Game entity is in charge of the relationship.
    @ManyToMany(mappedBy = "platforms")
    @ToString.Exclude // Avoid circular dependency in toString()
    @EqualsAndHashCode.Exclude // Avoid circular dependency in equals/hashCode
    private Set<Game> games = new HashSet<>();
}