package com.gamereleasetracker;

import com.gamereleasetracker.model.Platform;
import com.gamereleasetracker.repository.PlatformRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PlatformRepositoryIntegrationTest {

    @Autowired
    private PlatformRepository platformRepository;

    /**
     * Tests that a Platform entity can be saved and then retrieved from the database,
     * confirming its name is persisted correctly.
     */
    @Test
    @Transactional
    void shouldSaveAndFindPlatform() {
        // --- Setup ---
        Platform newPlatform = new Platform();
        newPlatform.setName("Nintendo Switch");

        // --- Action ---
        Platform savedPlatform = platformRepository.save(newPlatform);

        // --- Assertion ---
        Platform foundPlatform = platformRepository.findById(savedPlatform.getId()).orElseThrow();
        assertThat(foundPlatform.getName()).isEqualTo("Nintendo Switch");
    }

    /**
     * Verifies that the database's unique constraint on the 'name' column is working.
     * It expects a DataIntegrityViolationException when trying to save a platform
     * with a name that already exists.
     */
    @Test
    @Transactional
    void shouldThrowExceptionWhenSavingDuplicatePlatformName() {
        // --- Setup ---
        Platform platform1 = new Platform();
        platform1.setName("Xbox Series X");
        platformRepository.save(platform1);

        Platform platform2 = new Platform();
        platform2.setName("Xbox Series X"); // Duplicate name

        // --- Action & Assertion ---
        // Asserts that executing the save operation throws the expected exception.
        assertThrows(DataIntegrityViolationException.class, () -> {
            platformRepository.saveAndFlush(platform2);
        });
    }

    /**
     * Tests the findAll method to ensure it retrieves all saved Platform entities
     * from the database.
     */
    @Test
    @Transactional
    void shouldFindAllPlatforms() {
        // --- Setup ---
        Platform platform1 = new Platform();
        platform1.setName("PC");
        platformRepository.save(platform1);

        Platform platform2 = new Platform();
        platform2.setName("PS5");
        platformRepository.save(platform2);

        // --- Action ---
        List<Platform> platforms = platformRepository.findAll();

        // --- Assertion ---
        assertThat(platforms).hasSize(2);
        assertThat(platforms).extracting(Platform::getName)
                .containsExactlyInAnyOrder("PC", "PS5");
    }
}