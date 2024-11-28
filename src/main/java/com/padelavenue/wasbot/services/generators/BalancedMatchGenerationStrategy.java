package com.padelavenue.wasbot.services.generators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.padelavenue.wasbot.domain.Match;
import com.padelavenue.wasbot.exceptions.custom.ValidationException;
import com.padelavenue.wasbot.web.dto.PlayerDto;
import com.padelavenue.wasbot.web.mappers.PlayerMapper;

import lombok.RequiredArgsConstructor;

@Component("balancedStrategy")
@RequiredArgsConstructor
public class BalancedMatchGenerationStrategy implements MatchGenerationStrategy {

    private final PlayerMapper playerMapper;

    @Override
    public List<Match> generateWeeklyMatches(List<PlayerDto> availablePlayers) throws ValidationException {

        // Sort players by points
        List<PlayerDto> sortedPlayers = new ArrayList<>(availablePlayers);
        availablePlayers.sort((p1, p2) -> p2.getPoints().compareTo(p1.getPoints()));

        List<Match> matches = new ArrayList<>();
        
        while (sortedPlayers.size() >= 4) {
            Match match = new Match();
            
            // Distribute players to balance teams based on ranking
            // Team 1: Best player + Worst player of the current group
            // Team 2: Second best + Second worst
            match.setTeam1Player1(playerMapper.toEntity(sortedPlayers.get(0))); // Best
            match.setTeam1Player2(playerMapper.toEntity(sortedPlayers.get(3))); // Worst
            match.setTeam2Player1(playerMapper.toEntity(sortedPlayers.get(1))); // Second best
            match.setTeam2Player2(playerMapper.toEntity(sortedPlayers.get(2))); // Third
            
            match.setTeam1Won(null);
            matches.add(match);
            
            // Remove the already assigned players
            sortedPlayers = sortedPlayers.subList(4, sortedPlayers.size());
        }

        return matches;
    }
}
