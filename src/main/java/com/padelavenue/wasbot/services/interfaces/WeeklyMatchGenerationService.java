package com.padelavenue.wasbot.services.interfaces;

import java.util.List;

import com.padelavenue.wasbot.services.utils.MatchGenerationStrategyEnum;
import com.padelavenue.wasbot.web.dto.MatchResponseDto;

import jakarta.validation.ValidationException;

public interface WeeklyMatchGenerationService {
    /**
     * Generates random matches for active players for the current week.
     * 
     * @param strategy Match generation strategy
     * @return List of generated matches.
     */
    List<MatchResponseDto> generateWeeklyMatches(MatchGenerationStrategyEnum strategy) throws ValidationException;

    /**
     * Regenerates matches for the current week.
     * Used by admins when players can't play.
     * 
     * @return List of new matches
     */
    List<MatchResponseDto> regenerateWeeklyMatches();

    /**
     * Gets current week's matches.
     * 
     * @return List of current week's matches
     */
    List<MatchResponseDto> getCurrentWeekMatches();

    /**
     * Resets all matches and points.
     * Admin only operation.
     */
    void resetTournament();
}
