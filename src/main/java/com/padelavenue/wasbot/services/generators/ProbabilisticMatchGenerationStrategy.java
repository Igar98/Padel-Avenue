package com.padelavenue.wasbot.services.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.padelavenue.wasbot.domain.Match;
import com.padelavenue.wasbot.exceptions.custom.ValidationException;
import com.padelavenue.wasbot.web.dto.PlayerDto;
import com.padelavenue.wasbot.web.mappers.PlayerMapper;

import lombok.RequiredArgsConstructor;

//TODO: Review this implementation.
@Component("probabilisticStrategy")
@RequiredArgsConstructor
public class ProbabilisticMatchGenerationStrategy implements MatchGenerationStrategy {

    private final PlayerMapper playerMapper;

    private final Random random = new Random();

    @Override
    public List<Match> generateWeeklyMatches(List<PlayerDto> availablePlayers) throws ValidationException {

        List<Match> matches = new ArrayList<>();

        while (availablePlayers.size() >= 4) {
            // Calculate the total rating of the current group
            int totalPoints = availablePlayers.stream()
                    .mapToInt(PlayerDto::getPoints)
                    .sum();

            Match match = new Match();
            List<PlayerDto> matchPlayers = new ArrayList<>(4);

            // Select the first player randomly
            PlayerDto firstPlayer = selectPlayerByProbability(availablePlayers, totalPoints);
            matchPlayers.add(firstPlayer);
            availablePlayers.remove(firstPlayer);

            // Update total points
            totalPoints = availablePlayers.stream()
                    .mapToInt(PlayerDto::getPoints)
                    .sum();

            // Select a partner to balance the team
            // Find a player with complementary points
            int targetPoints = getTotalAveragePoints(availablePlayers) - firstPlayer.getPoints();
            PlayerDto partner = selectPlayerByTargetPoints(availablePlayers, targetPoints);
            matchPlayers.add(partner);
            availablePlayers.remove(partner);

            // For the second team, repeat the process
            totalPoints = availablePlayers.stream()
                    .mapToInt(PlayerDto::getPoints)
                    .sum();

            // Select the first player for the second team
            PlayerDto thirdPlayer = selectPlayerByProbability(availablePlayers, totalPoints);
            matchPlayers.add(thirdPlayer);
            availablePlayers.remove(thirdPlayer);

            // Select the last player to balance the team
            PlayerDto fourthPlayer = selectPlayerByTargetPoints(availablePlayers,
                    getTotalAveragePoints(availablePlayers) - thirdPlayer.getPoints());
            matchPlayers.add(fourthPlayer);
            availablePlayers.remove(fourthPlayer);

            // Assign players to the match
            match.setTeam1Player1(playerMapper.toEntity(matchPlayers.get(0)));
            match.setTeam1Player2(playerMapper.toEntity(matchPlayers.get(1)));
            match.setTeam2Player1(playerMapper.toEntity(matchPlayers.get(2)));
            match.setTeam2Player2(playerMapper.toEntity(matchPlayers.get(3)));
            match.setTeam1Won(null);

            matches.add(match);
        }

        return matches;
    }

    private PlayerDto selectPlayerByProbability(List<PlayerDto> players, int totalPoints) {
        // If totalPoints is 0, select randomly
        if (totalPoints == 0) {
            return players.get(random.nextInt(players.size()));
        }
        
        // Calculate probabilities inversely proportional to the points
        double[] probabilities = new double[players.size()];
        for (int i = 0; i < players.size(); i++) {
            // Use totalPoints + 1 to avoid division by zero
            probabilities[i] = (double)(totalPoints - players.get(i).getPoints()) / (totalPoints + 1);
        }
        
        // Normalize probabilities
        double sum = Arrays.stream(probabilities).sum();
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        
        // Select player based on probabilities
        double rand = random.nextDouble();
        double cumSum = 0.0;
        for (int i = 0; i < probabilities.length; i++) {
            cumSum += probabilities[i];
            if (rand <= cumSum) {
            return players.get(i);
            }
        }
        
        // In case of rounding error, return the last player
        return players.get(players.size() - 1);

    }

    private PlayerDto selectPlayerByTargetPoints(List<PlayerDto> players, int targetPoints) {
        return players.stream()
            .min((p1, p2) -> {
                int diff1 = Math.abs(p1.getPoints() - targetPoints);
                int diff2 = Math.abs(p2.getPoints() - targetPoints);
                return Integer.compare(diff1, diff2);
            })
            .orElse(players.get(0));
    }

    private int getTotalAveragePoints(List<PlayerDto> players) {
        if (players.isEmpty()) return 0;
        return players.stream()
            .mapToInt(PlayerDto::getPoints)
            .sum() / players.size();
    }

}
