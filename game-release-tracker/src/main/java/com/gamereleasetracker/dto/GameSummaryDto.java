package com.gamereleasetracker.dto;

import com.gamereleasetracker.model.GameStatus;
import java.time.Instant;

public record GameSummaryDto(
        Long id,
        String title,
        String coverImageUrl,
        Instant releaseDate,
        GameStatus status
) {}