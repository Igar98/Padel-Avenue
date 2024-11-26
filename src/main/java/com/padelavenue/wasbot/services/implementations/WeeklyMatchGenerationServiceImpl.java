package com.padelavenue.wasbot.services.implementations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.padelavenue.wasbot.domain.Match;
import com.padelavenue.wasbot.repositories.MatchRepository;
import com.padelavenue.wasbot.services.interfaces.PlayerService;
import com.padelavenue.wasbot.services.interfaces.WeeklyMatchGenerationService;
import com.padelavenue.wasbot.services.utils.MatchGenerationStrategyEnum;
import com.padelavenue.wasbot.web.dto.MatchResponseDto;
import com.padelavenue.wasbot.web.dto.PlayerDto;
import com.padelavenue.wasbot.web.mappers.MatchMapper;
import com.padelavenue.wasbot.web.mappers.PlayerMapper;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeeklyMatchGenerationServiceImpl implements WeeklyMatchGenerationService {

    private final PlayerService playerService;
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final PlayerMapper playerMapper;
    private final Random random = new Random();

    @Override
    @Transactional
    public List<MatchResponseDto> generateWeeklyMatches(MatchGenerationStrategyEnum strategy) throws ValidationException {
        List<PlayerDto> activePlayers = playerService.getAllActivePlayers();
        
        if (activePlayers.size() < 4) {
            throw new ValidationException("Not enough active players to generate matches");
        }

        List<Match> matches = switch(strategy) {
            case RANDOM -> generateRandomMatches(activePlayers);
            case BALANCED -> generateBalancedMatches(activePlayers);
            case PROBABILISTIC -> generateProbabilisticMatches(activePlayers);
        };

        return matches.stream()
                     .map(matchRepository::save)
                     .map(matchMapper::toResponseDto)
                     .toList();
    }

    private List<Match> generateRandomMatches(List<PlayerDto> players) {
        List<PlayerDto> shuffledPlayers = new ArrayList<>(players);
        Collections.shuffle(shuffledPlayers, random);
        
        return createMatchesFromPlayerList(shuffledPlayers);
    }

    private List<Match> generateBalancedMatches(List<PlayerDto> players) {
        // Sort players by points
        List<PlayerDto> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort((p1, p2) -> p2.getPoints().compareTo(p1.getPoints()));

        List<Match> matches = new ArrayList<>();
        
        while (sortedPlayers.size() >= 4) {
            Match match = new Match();
            
            // Distribuir jugadores para equilibrar equipos basado en ranking
            // Equipo 1: Mejor jugador + Peor jugador del grupo actual
            // Equipo 2: Segundo mejor + Segundo peor
            match.setTeam1Player1(playerMapper.toEntity(sortedPlayers.get(0))); // Mejor
            match.setTeam1Player2(playerMapper.toEntity(sortedPlayers.get(3))); // Peor
            match.setTeam2Player1(playerMapper.toEntity(sortedPlayers.get(1))); // Segundo mejor
            match.setTeam2Player2(playerMapper.toEntity(sortedPlayers.get(2))); // Tercero
            
            match.setTeam1Won(null);
            matches.add(match);
            
            // Remover los jugadores ya asignados
            sortedPlayers = sortedPlayers.subList(4, sortedPlayers.size());
        }

        return matches;
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

    private List<Match> generateProbabilisticMatches(List<PlayerDto> players) {
        List<Match> matches = new ArrayList<>();
        List<PlayerDto> remainingPlayers = new ArrayList<>(players);
        
        while (remainingPlayers.size() >= 4) {
            // Calculamos el rating total del grupo actual
            int totalPoints = remainingPlayers.stream()
                .mapToInt(PlayerDto::getPoints)
                .sum();
            
            Match match = new Match();
            List<PlayerDto> matchPlayers = new ArrayList<>(4);
            
            // Seleccionar primer jugador aleatoriamente
            PlayerDto firstPlayer = selectPlayerByProbability(remainingPlayers, totalPoints);
            matchPlayers.add(firstPlayer);
            remainingPlayers.remove(firstPlayer);
            
            // Actualizar puntos totales
            totalPoints = remainingPlayers.stream()
                .mapToInt(PlayerDto::getPoints)
                .sum();
            
            // Seleccionar compañero que mejor equilibre el equipo
            // Buscamos jugador con puntuación complementaria
            int targetPoints = getTotalAveragePoints(remainingPlayers) - firstPlayer.getPoints();
            PlayerDto partner = selectPlayerByTargetPoints(remainingPlayers, targetPoints);
            matchPlayers.add(partner);
            remainingPlayers.remove(partner);
            
            // Para el segundo equipo, repetimos el proceso
            totalPoints = remainingPlayers.stream()
                .mapToInt(PlayerDto::getPoints)
                .sum();
            
            // Seleccionar primer jugador del segundo equipo
            PlayerDto thirdPlayer = selectPlayerByProbability(remainingPlayers, totalPoints);
            matchPlayers.add(thirdPlayer);
            remainingPlayers.remove(thirdPlayer);
            
            // Seleccionar último jugador para equilibrar
            PlayerDto fourthPlayer = selectPlayerByTargetPoints(remainingPlayers, 
                getTotalAveragePoints(remainingPlayers) - thirdPlayer.getPoints());
            matchPlayers.add(fourthPlayer);
            remainingPlayers.remove(fourthPlayer);
            
            // Asignar jugadores al partido
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
        // Si totalPoints es 0, seleccionar aleatoriamente
        if (totalPoints == 0) {
            return players.get(random.nextInt(players.size()));
        }
        
        // Calcular probabilidades inversamente proporcionales a los puntos
        double[] probabilities = new double[players.size()];
        for (int i = 0; i < players.size(); i++) {
            // Usamos totalPoints + 1 para evitar división por cero
            probabilities[i] = (double)(totalPoints - players.get(i).getPoints()) / (totalPoints + 1);
        }
        
        // Normalizar probabilidades
        double sum = Arrays.stream(probabilities).sum();
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        
        // Seleccionar jugador basado en probabilidades
        double rand = random.nextDouble();
        double cumSum = 0.0;
        for (int i = 0; i < probabilities.length; i++) {
            cumSum += probabilities[i];
            if (rand <= cumSum) {
                return players.get(i);
            }
        }
        
        // En caso de error de redondeo, devolver el último
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
        //TODO: Hardwired change when strategy is selected
        return generateWeeklyMatches(MatchGenerationStrategyEnum.RANDOM);
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