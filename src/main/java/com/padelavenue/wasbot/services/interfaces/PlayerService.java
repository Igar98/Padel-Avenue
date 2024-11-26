package com.padelavenue.wasbot.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.padelavenue.wasbot.exceptions.custom.ResourceNotFoundException;
import com.padelavenue.wasbot.exceptions.custom.ValidationException;
import com.padelavenue.wasbot.web.dto.LeaderboardEntryDto;
import com.padelavenue.wasbot.web.dto.PlayerDto;

import jakarta.transaction.Transactional;

public interface PlayerService {

    /**
     * Get all players.
     * 
     * @return List of all players.
     * @throws ResourceNotFoundException.
     */
    public List<PlayerDto> getAllPlayers();

    /**
     * Get all active players.
     * 
     * @return List of all active players.
     * @throws ResourceNotFoundException.
     */
    public List<PlayerDto> getAllActivePlayers();

    /**
     * Get player by id.
     * 
     * @param id Player id.
     * @return Player.
     * @throws ResourceNotFoundException.
     */
    public PlayerDto getPlayerById(UUID id) throws ResourceNotFoundException;

    /**
     * Get player by phone.
     * 
     * @param phone Player phone.
     * @return Player.
     * @throws ResourceNotFoundException.
     */
    public PlayerDto getPlayerByPhone(String phone) throws ResourceNotFoundException;

    /**
     * Set player points.
     * 
     * @param points   Points.
     * @param playerId Player id.
     * @throws ResourceNotFoundException.
     */
    public void setPlayerPoints(Integer points, UUID playerId) throws ResourceNotFoundException;

    /**
     * Create player.
     * 
     * @param player Player.
     * @return Player.
     * @throws ValidationException.
     */
    @Transactional
    public PlayerDto createPlayer(PlayerDto playerDto) throws ValidationException;

    /**
     * Update player.
     * 
     * @param id            Player id.
     * @param playerDetails Player details.
     * @return Player.
     * @throws ResourceNotFoundException.
     * @throws ValidationException.
     */
    @Transactional
    public PlayerDto updatePlayer(UUID id, PlayerDto playerDetails) throws ResourceNotFoundException, ValidationException;

    /**
     * Delete player.
     * 
     * @param id Player id.
     * @throws ResourceNotFoundException.
     */
    @Transactional
    public void deletePlayer(UUID id) throws ResourceNotFoundException;

    /**
     * Disable player.
     * 
     * @param id Player id.
     * @throws ResourceNotFoundException.
     */
    @Transactional
    public void disablePlayer(UUID id) throws ResourceNotFoundException;

    /**
     * Get leaderboard.
     * 
     * @return Leaderboard.
     */
    public List<LeaderboardEntryDto> getLeaderboard();

    /**
     * Reset all player points.
     */
    public void resetAllPlayerPoints();
}
