package com.gamereleasetracker.dto;

import com.gamereleasetracker.model.GameStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

// A complete representation of a game for its detail page.
public record GameDetailDto(
        Long id,
        String title,
        String description,
        String coverImageUrl,
        Instant releaseDate,
        String developer,
        String publisher,
        String rawgGameSlug,
        GameStatus status,
        String ageRating,
        Set<PlatformDto> platforms,
        Set<PreorderLinkDto> preorderLinks
) {}