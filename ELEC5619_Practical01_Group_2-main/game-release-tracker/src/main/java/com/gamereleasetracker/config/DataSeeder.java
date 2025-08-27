/**
 * Seeds the database with initial data required for the application to function correctly.
 * This component runs on application startup, via the CommandLineRunner
 * interface. Its primary responsibility is to check for the existence of essential data,
 * such as user roles, and create them if they are not already present. This ensures
 * that the foundational data is always available, preventing potential errors and
 * simplifying initial setup.
 * In its current implementation, it specifically handles the creation of the
 * ROLE_USER and ROLE_ADMIN roles. It first checks if any roles
 * exist in the database to avoid duplicating entries on subsequent application restarts.
 */
package com.gamereleasetracker.config;

import com.gamereleasetracker.model.Role;
import com.gamereleasetracker.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Tells Spring to manage this class as one of its beans.
/*
A Spring Bean is simply a Java object that is created, assembled, and managed by
the Spring Framework's Inversion of Control (IoC) container. Instead of you creating objects manually,
you tell the Spring container about the object, and the container handles its entire lifecycle for you. */
@Component
public class DataSeeder implements CommandLineRunner {
// CommandLineRunner requires run method, which Spring will execute automatically on startup.
    private final RoleRepository roleRepository;

    // Spring will automatically inject the RoleRepository for us
    public DataSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if roles are already in the database
        if (roleRepository.count() == 0) {
            // Create ROLE_USER
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);

            // Create ROLE_ADMIN
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            System.out.println("Roles have been seeded to the database.");
        } else {
            System.out.println("Roles already exist in the database.");
        }
    }
}