package com.padelavenue.wasbot.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntryDto {
    private Long playerId;
    private String playerName;
    private Integer points;
    private Integer matchesPlayed;
    private Integer matchesWon;
}
