package com.padelavenue.wasbot.web.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.padelavenue.wasbot.services.interfaces.MatchService;
import com.padelavenue.wasbot.web.dto.MatchResponseDto;
import com.padelavenue.wasbot.web.mappers.MatchMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
@Tag(name = "Matches", description = "Match management endpoints")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/week")
    @Operation(summary = "Get matches for a specific week")
    public ResponseEntity<List<MatchResponseDto>> getMatchesByWeek(
            @Parameter(description = "Week start date (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekOf) {
        return ResponseEntity.ok(matchService.getMatchesByWeek(weekOf));
    }

    @GetMapping("/player/{playerId}")
    @Operation(summary = "Get matches for a specific player")
    public ResponseEntity<List<MatchResponseDto>> getPlayerMatches(
            @Parameter(description = "Player ID") @PathVariable UUID playerId) {
        return ResponseEntity.ok(matchService.getPlayerMatches(playerId));
    }

    // @PostMapping
    // @Operation(summary = "Register a new match")
    // public ResponseEntity<MatchResponseDto> registerMatch(
    //         @Valid @RequestBody MatchRequestDTO matchDTO) {
    //     return new ResponseEntity<>(matchService.registerMatch(matchDTO),
    //             HttpStatus.CREATED);
    // }
}