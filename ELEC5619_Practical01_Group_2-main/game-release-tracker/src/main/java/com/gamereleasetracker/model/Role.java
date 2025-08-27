package com.gamereleasetracker.model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user role in the application.
 * This class is a JPA entity that maps to the 'roles' table in the database.
 */

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 'unique = true' ensures that every role name in the database is unique.
    @Column(length = 20, unique = true, nullable = false)
    private String name;

    // One role can be associated with many users.
    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();

}