package com.gamereleasetracker.dto;

public record PreorderLinkDto(
        Long id,
        Long gameId,
        String storeName,
        String url
) {}