package com.gamereleasetracker.dto;

// A secure representation of a user for API responses.
public record UserDto(
        Long id,
        String username,
        String email,
        boolean enableNotifications
) {}