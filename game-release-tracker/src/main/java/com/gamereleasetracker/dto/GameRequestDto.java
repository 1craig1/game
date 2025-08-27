package com.gamereleasetracker.dto;

import com.gamereleasetracker.model.GameStatus;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
import java.math.BigDecimal;
import java.time.Instant;

public record GameRequestDto(
        // Ensures the title is not null and contains actual text.
        @NotBlank(message = "Title cannot be blank")
        @Size(max = 255)
        String title,

        String description,

        // Validates that the string is a well-formed URL.
        @URL(message = "Cover image must be a valid URL")
        String coverImageUrl,

        // Ensures the release date field is not null.
        @NotNull(message = "Release date cannot be null")
        // Validates that the provided date and time is in the future.
        @Future(message = "Release date must be in the future")
        Instant releaseDate,

        @PositiveOrZero(message = "Price must be a positive value or zero")
        BigDecimal price,

        @NotNull(message = "Status cannot be null")
        GameStatus status
) {}