package com.gamereleasetracker.dto.api_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiGameDto {

    private String slug;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate released;

    @JsonProperty("background_image")
    private String backgroundImage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updated;

    private String esrbRating;
    @JsonProperty("esrb_rating")
    private void unpackEsrbRating(Map<String, Object> esrbRating) {
        this.esrbRating = esrbRating != null ? (String) esrbRating.get("name") : null;
    }

    // Getters and setters
    public String getSlug() { return slug; }
    
    public String getName() { return name; }
    
    public LocalDate getReleased() { return released; }
    
    public String getBackgroundImage() { return backgroundImage; }
    
    public LocalDateTime getUpdated() { return updated; }

    public String getEsrbRating() { return esrbRating; }
}
