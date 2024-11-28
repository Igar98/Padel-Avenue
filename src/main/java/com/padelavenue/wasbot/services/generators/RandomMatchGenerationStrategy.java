package com.padelavenue.wasbot.services.generators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.padelavenue.wasbot.domain.Match;
import com.padelavenue.wasbot.exceptions.custom.ValidationException;
import com.padelavenue.wasbot.web.dto.PlayerDto;
import com.padelavenue.wasbot.web.mappers.PlayerMapper;

import lombok.RequiredArgsConstructor;

@Component("randomStrategy")
@RequiredArgsConstructor
public class RandomMatchGenerationStrategy implements MatchGenerationStrategy {

    private final PlayerMapper playerMapper;

    private final Random random = new Random();

    @Override
    public List<Match> generateWeeklyMatches(List<PlayerDto> availablePlayers) throws ValidationException {

        Collections.shuffle(availablePlayers, random);

        return createMatchesFromPlayerList(availablePlayers);
    }

    private List<Match> createMatchesFromPlayerList(List<PlayerDto> players) {
        List<Match> matches = new ArrayList<>();

        for (int i = 0; i < players.size(); i += 4) {
            if (i + 3 < players.size()) {
                Match match = new Match();
                match.setTeam1Player1(playerMapper.toEntity(players.get(i)));
                match.setTeam1Player2(playerMapper.toEntity(players.get(i + 1)));
                match.setTeam2Player1(playerMapper.toEntity(players.get(i + 2)));
                match.setTeam2Player2(playerMapper.toEntity(players.get(i + 3)));
                match.setTeam1Won(null);
                matches.add(match);
            }
        }

        return matches;
    }

}
