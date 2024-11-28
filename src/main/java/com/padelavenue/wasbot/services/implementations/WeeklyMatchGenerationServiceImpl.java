package com.padelavenue.wasbot.services.implementations;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.padelavenue.wasbot.domain.Match;
import com.padelavenue.wasbot.repositories.MatchRepository;
import com.padelavenue.wasbot.services.generators.MatchGenerationStrategy;
import com.padelavenue.wasbot.services.generators.MatchGenerationStrategyFactory;
import com.padelavenue.wasbot.services.interfaces.PlayerService;
import com.padelavenue.wasbot.services.interfaces.WeeklyMatchGenerationService;
import com.padelavenue.wasbot.web.dto.MatchResponseDto;
import com.padelavenue.wasbot.web.dto.PlayerDto;
import com.padelavenue.wasbot.web.mappers.MatchMapper;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeeklyMatchGenerationServiceImpl implements WeeklyMatchGenerationService {

    private final PlayerService playerService;
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final MatchGenerationStrategyFactory strategyFactory;

    @Override
    @Transactional
    public List<MatchResponseDto> generateWeeklyMatches() throws ValidationException {
        List<PlayerDto> activePlayers = playerService.getAllActivePlayers();
        
        // Check if there are enough players to generate matches.
        if (activePlayers.size() < 4) {
            throw new ValidationException("Not enough active players to generate matches");
        }

        // Generate matches based on selected strategy.
        MatchGenerationStrategy strategy = strategyFactory.getStrategy();
        List<Match> matches = strategy.generateWeeklyMatches(activePlayers);

        return matches.stream()
                     .map(matchRepository::save)
                     .map(matchMapper::toResponseDto)
                     .toList();
    }

    

    @Override
    @Transactional
    public List<MatchResponseDto> regenerateWeeklyMatches() {
        // Delete current week's matches that haven't been played yet
        LocalDate currentWeekStart = LocalDate.now().with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        List<Match> currentMatches = matchRepository.findByWeekOf(currentWeekStart);
        currentMatches.stream()
                     .filter(m -> m.getTeam1Won() == null)
                     .forEach(matchRepository::delete);
        
        // Generate new matches
        return generateWeeklyMatches();
    }

    @Override
    public List<MatchResponseDto> getCurrentWeekMatches() {
        LocalDate currentWeekStart = LocalDate.now().with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        return matchRepository.findByWeekOf(currentWeekStart).stream()
                            .map(matchMapper::toResponseDto)
                            .toList();
    }

    @Override
    @Transactional
    public void resetTournament() {
        // Delete all matches
        matchRepository.deleteAll();
        
        // Reset all player points
        playerService.resetAllPlayerPoints();
    }
}