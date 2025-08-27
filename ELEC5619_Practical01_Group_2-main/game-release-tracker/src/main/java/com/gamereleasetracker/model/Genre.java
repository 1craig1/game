package com.gamereleasetracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.HashSet;
import java.util.Set;

/** 
 * Represents a genre for each game (e.g., Action, RPG)
 */
@Entity
@Table(name = "genres")
@Data
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "genres")
    @ToString.Exclude // Avoid circular dependency in toString()
    @EqualsAndHashCode.Exclude // Avoid circular dependency in equals/hashCode
    private Set<Game> games = new HashSet<>();
}