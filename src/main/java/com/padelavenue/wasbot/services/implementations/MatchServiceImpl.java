package com.padelavenue.wasbot.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.padelavenue.wasbot.domain.Match;
import com.padelavenue.wasbot.domain.Player;
import com.padelavenue.wasbot.exceptions.custom.ResourceNotFoundException;
import com.padelavenue.wasbot.repositories.MatchRepository;
import com.padelavenue.wasbot.repositories.PlayerRepository;
import com.padelavenue.wasbot.services.interfaces.MatchService;
import com.padelavenue.wasbot.services.interfaces.PlayerService;
import com.padelavenue.wasbot.web.dto.MatchResponseDto;
import com.padelavenue.wasbot.web.dto.PlayerDto;
import com.padelavenue.wasbot.web.mappers.MatchMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final PlayerService playerService;
    private final MatchMapper matchMapper;

    @Override
    public List<MatchResponseDto> getMatchesByWeek(LocalDate weekOf) {
        return matchRepository.findByWeekOf(weekOf).stream().map(matchMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchResponseDto> getPlayerMatches(UUID playerId){
        return matchRepository.findMatchesByPlayerId(playerId).stream().map(matchMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // @Transactional
    // public Match registerMatch(Match match) {
    //     validateMatch(match);
    //     Match savedMatch = matchRepository.save(match);
    //     updatePlayerPoints(savedMatch);
    //     return savedMatch;
    // }

    // private void validateMatch(Match match) {
    //     // Verificar que todos los jugadores son diferentes
    //     Set<Long> playerIds = Set.of(
    //             match.getTeam1Player1().getId(),
    //             match.getTeam1Player2().getId(),
    //             match.getTeam2Player1().getId(),
    //             match.getTeam2Player2().getId());

    //     if (playerIds.size() != 4) {
    //         throw new InvalidMatchException("All players must be different");
    //     }

    //     // Verificar que todos los jugadores existen y están activos
    //     for (Long id : playerIds) {
    //         Player player = playerRepository.findById(id)
    //                 .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + id));
    //         if (!player.getIsActive()) {
    //             throw new InvalidMatchException("Player " + player.getName() + " is not active");
    //         }
    //     }
    // }

    // private void updatePlayerPoints(Match match) {
    //     if (match.getTeam1Won()) {
    //         incrementPoints(match.getTeam1Player1());
    //         incrementPoints(match.getTeam1Player2());
    //     } else {
    //         incrementPoints(match.getTeam2Player1());
    //         incrementPoints(match.getTeam2Player2());
    //     }
    // }

    // private void incrementPoints(Player player) {
    //     player.setPoints(player.getPoints() + 1);
    //     playerRepository.save(player);
    // }

    /*
     * Helper method to check if a match exists in the database.
     * If not, it throws a ResourceNotFoundException.
     * If it exists, it returns the match.
     */
    private Match findMatchOrThrow(UUID id) throws ResourceNotFoundException {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + id));
    }
}
