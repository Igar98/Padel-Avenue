package com.padelavenue.wasbot.services.implementations;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.padelavenue.wasbot.domain.Player;
import com.padelavenue.wasbot.exceptions.custom.ResourceNotFoundException;
import com.padelavenue.wasbot.exceptions.custom.ValidationException;
import com.padelavenue.wasbot.repositories.PlayerRepository;
import com.padelavenue.wasbot.services.interfaces.PlayerService;
import com.padelavenue.wasbot.web.dto.LeaderboardEntryDto;
import com.padelavenue.wasbot.web.dto.PlayerDto;
import com.padelavenue.wasbot.web.mappers.PlayerMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @Override
    public List<PlayerDto> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(playerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerDto> getAllActivePlayers() {
        return playerRepository.findByIsActiveTrue().stream()
                .map(playerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerDto getPlayerById(UUID id) throws ResourceNotFoundException {
        return playerMapper.toDto(findPlayerOrThrow(id));
    }

    @Override
    public PlayerDto getPlayerByPhone(String phone) throws ResourceNotFoundException {
        return playerMapper.toDto(playerRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with phone: " + phone)));
    }

    @Override
    public void setPlayerPoints(Integer points, UUID playerId) throws ResourceNotFoundException {
        Player player = findPlayerOrThrow(playerId);
        player.setPoints(points);
        playerRepository.save(player);
    }

    @Override
    @Transactional
    public PlayerDto createPlayer(PlayerDto playerDto) throws ValidationException {
        // Check if phone or name already exists
        if (playerRepository.findByPhone(playerDto.getPhone()).isPresent()) {
            throw new ValidationException("Player already exists with phone: " + playerDto.getPhone());
        }
        if (playerRepository.findByName(playerDto.getName()).isPresent()) {
            throw new ValidationException("Player already exists with name: " + playerDto.getName());
        }
        return playerMapper.toDto(playerRepository.save(playerMapper.toEntity(playerDto)));
    }

    @Override
    @Transactional
    public PlayerDto updatePlayer(UUID id, PlayerDto playerDetails)
            throws ResourceNotFoundException, ValidationException {
        Player player = findPlayerOrThrow(id);

        // Check if new name or phone already exists
        playerRepository.findByName(playerDetails.getName())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new ValidationException("Name already in use: " + playerDetails.getName());
                });

        playerRepository.findByPhone(playerDetails.getPhone())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new ValidationException("Phone already in use: " + playerDetails.getPhone());
                });

        playerMapper.updatePlayerFromDto(playerDetails, player);

        return playerMapper.toDto(playerRepository.save(player));
    }

    @Override
    @Transactional
    public void deletePlayer(UUID id) throws ResourceNotFoundException {
        playerRepository.delete(findPlayerOrThrow(id));
    }

    @Override
    @Transactional
    public void disablePlayer(UUID id) throws ResourceNotFoundException {
        Player player = findPlayerOrThrow(id);
        player.setIsActive(false);
        playerRepository.save(player);
    }

    @Override
    public List<LeaderboardEntryDto> getLeaderboard() {
        return playerRepository.getLeaderboard();
    }

    @Override
    public void resetAllPlayerPoints() {
        playerRepository.resetAllPlayerPoints();
    }

    /*
     * Helper method to check if a player exists in the database.
     * If not, it throws a ResourceNotFoundException.
     * If it exists, it returns the product.
     */
    private Player findPlayerOrThrow(UUID id) throws ResourceNotFoundException {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + id));
    }
}
