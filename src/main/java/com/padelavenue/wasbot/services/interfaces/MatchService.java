package com.padelavenue.wasbot.services.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;

import com.padelavenue.wasbot.domain.Match;
import com.padelavenue.wasbot.domain.Player;
import com.padelavenue.wasbot.exceptions.custom.ResourceNotFoundException;
import com.padelavenue.wasbot.exceptions.custom.ValidationException;
import com.padelavenue.wasbot.web.dto.MatchResponseDto;

public interface MatchService {
    public List<MatchResponseDto> getMatchesByWeek(LocalDate weekOf);
    
    public List<MatchResponseDto> getPlayerMatches(UUID playerId);
    
    // public Match registerMatch(Match match) throws ValidationException;
}
