package com.padelavenue.wasbot.web.dto;

import java.util.UUID;

public record LeaderboardEntryDto(
    UUID playerId,
    String playerName,
    Integer points,
    Long matchesPlayed,
    Long matchesWon
) {}