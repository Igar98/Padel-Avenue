package com.padelavenue.wasbot.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchResponseDto {
    private LocalDateTime playedAt;

    private LocalDate weekOf;

    private UUID team1Player1Name;

    private UUID team1Player2Name;

    private UUID team2Player1Name;

    private UUID team2Player2Name;

    private Boolean team1Won;
}
