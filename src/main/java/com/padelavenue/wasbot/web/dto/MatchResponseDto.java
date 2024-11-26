package com.padelavenue.wasbot.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class MatchResponseDto {
    private LocalDateTime playedAt;

    private LocalDate weekOf;

    private UUID team1Player1Name;

    private UUID team1Player2Name;

    private UUID team2Player1Name;

    private UUID team2Player2Name;

    private Boolean team1Won;
}
