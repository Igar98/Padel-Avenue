package com.padelavenue.wasbot.services.generators;

import java.util.List;

import com.padelavenue.wasbot.domain.Match;
import com.padelavenue.wasbot.exceptions.custom.ValidationException;
import com.padelavenue.wasbot.web.dto.PlayerDto;

public interface MatchGenerationStrategy {

    List<Match> generateWeeklyMatches(List<PlayerDto> availablePlayers) throws ValidationException;
}
