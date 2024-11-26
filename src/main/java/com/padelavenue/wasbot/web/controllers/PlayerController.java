package com.padelavenue.wasbot.web.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.padelavenue.wasbot.services.interfaces.PlayerService;
import com.padelavenue.wasbot.web.dto.LeaderboardEntryDto;
import com.padelavenue.wasbot.web.dto.PlayerDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
@Tag(name = "Players", description = "Player management endpoints")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    @Operation(summary = "Get all active players")
    @ApiResponse(responseCode = "200", description = "List of all active players")
    public ResponseEntity<List<PlayerDto>> getAllPlayers() {
        return ResponseEntity.ok(
                playerService.getAllPlayers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get player by ID")
    @ApiResponse(responseCode = "200", description = "Player found")
    @ApiResponse(responseCode = "404", description = "Player not found")
    public ResponseEntity<PlayerDto> getPlayer(
            @Parameter(description = "Player ID") @PathVariable UUID id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new player")
    @ApiResponse(responseCode = "201", description = "Player created")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<PlayerDto> createPlayer(
            @Valid @RequestBody PlayerDto playerDTO) {
        return new ResponseEntity<>(playerService.createPlayer(playerDTO),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing player")
    @ApiResponse(responseCode = "200", description = "Player updated")
    @ApiResponse(responseCode = "404", description = "Player not found")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<PlayerDto> updatePlayer(
            @Parameter(description = "Player ID") @PathVariable UUID id,
            @Valid @RequestBody PlayerDto playerDTO) {
        return ResponseEntity.ok(playerService.updatePlayer(id, playerDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate a player")
    @ApiResponse(responseCode = "204", description = "Player deactivated")
    @ApiResponse(responseCode = "404", description = "Player not found")
    public ResponseEntity<Void> deletePlayer(
            @Parameter(description = "Player ID") @PathVariable UUID id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/leaderboard")
    @Operation(summary = "Get global leaderboard")
    @ApiResponse(responseCode = "200", description = "Global leaderboard")
    public ResponseEntity<List<LeaderboardEntryDto>> getGlobalLeaderBoard() {
        return ResponseEntity.ok(
                playerService.getLeaderboard());
    }
    
}
