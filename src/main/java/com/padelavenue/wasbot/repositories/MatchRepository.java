package com.padelavenue.wasbot.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.padelavenue.wasbot.domain.Match;
import com.padelavenue.wasbot.domain.Player;

public interface MatchRepository extends JpaRepository<Match, UUID> {

    // Get matches of a specific week
    List<Match> findByWeekOf(LocalDate weekOf);
    
    //TODO: Can be done more efficiently with a single query?
    // Get all matches where a player participated
    @Query("SELECT m FROM Match m WHERE m.team1Player1.id = ?1 OR m.team1Player2.id = ?1 OR m.team2Player1.id = ?1 OR m.team2Player2.id = ?1")
    List<Match> findMatchesByPlayerId(UUID id);
    
    //TODO: Can be done more efficiently with a single query?
    // Get matches won by a player
    @Query("SELECT m FROM Match m WHERE " +
           "(m.team1Won = true AND (m.team1Player1 = ?1 OR m.team1Player2 = ?1)) OR " +
           "(m.team1Won = false AND (m.team2Player1 = ?1 OR m.team2Player2 = ?1))")
    List<Match> findWonMatchesByPlayer(Player player);
}
