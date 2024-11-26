package com.padelavenue.wasbot.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.padelavenue.wasbot.domain.Player;
import com.padelavenue.wasbot.web.dto.LeaderboardEntryDto;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    Optional<Player> findByPhone(String phone);

    Optional<Player> findByName(String name);

    List<Player> findByIsActiveTrue();

    @Query("UPDATE Player p SET p.points = 0")
    void resetAllPlayerPoints();

    @Query("""
        SELECT p.id as playerId,
               p.name as playerName,
               p.points as points,
               COUNT(DISTINCT m) as matchesPlayed,
               COUNT(DISTINCT CASE 
                   WHEN (m.team1Won = true AND (m.team1Player1 = p OR m.team1Player2 = p)) OR 
                        (m.team1Won = false AND (m.team2Player1 = p OR m.team2Player2 = p))
                   THEN m.id 
                   ELSE null 
               END) as matchesWon
        FROM Player p 
        LEFT JOIN Match m ON p = m.team1Player1 OR p = m.team1Player2 OR 
                            p = m.team2Player1 OR p = m.team2Player2 
        WHERE p.isActive = true 
        GROUP BY p.id, p.name, p.points 
        ORDER BY p.points DESC
        """)
    List<LeaderboardEntryDto> getLeaderboard();
}
