package com.padelavenue.wasbot.web.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.padelavenue.wasbot.services.interfaces.WeeklyMatchGenerationService;
import com.padelavenue.wasbot.services.utils.MatchGenerationStrategyEnum;
import com.padelavenue.wasbot.web.dto.MatchResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/matches/generation")
@RequiredArgsConstructor
@Tag(name = "Match Generation", description = "Match generation and management endpoints")
public class MatchGenerationController {

    private final WeeklyMatchGenerationService matchGenerationService;

    @PostMapping("/generate")
    @Operation(summary = "Generate matches for the current week")
    @ApiResponse(responseCode = "200", description = "Matches generated successfully")
    public ResponseEntity<List<MatchResponseDto>> generateMatches(
            @Parameter(description = "Generation strategy (RANDOM, BALANCED or PROBABILISTIC)") 
            @RequestParam(defaultValue = "RANDOM") MatchGenerationStrategyEnum strategy) {
        return ResponseEntity.ok(matchGenerationService.generateWeeklyMatches(strategy));
    }

    @PostMapping("/regenerate")
    @Operation(summary = "Regenerate matches for the current week")
    @ApiResponse(responseCode = "200", description = "Matches regenerated successfully")
    public ResponseEntity<List<MatchResponseDto>> regenerateMatches() {
        return ResponseEntity.ok(matchGenerationService.regenerateWeeklyMatches());
    }

    @GetMapping("/current")
    @Operation(summary = "Get current week's matches")
    @ApiResponse(responseCode = "200", description = "Current week's matches retrieved successfully")
    public ResponseEntity<List<MatchResponseDto>> getCurrentWeekMatches() {
        return ResponseEntity.ok(matchGenerationService.getCurrentWeekMatches());
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset tournament - Admin only")
    @ApiResponse(responseCode = "204", description = "Tournament reset successfully")
    public ResponseEntity<Void> resetTournament() {
        matchGenerationService.resetTournament();
        return ResponseEntity.noContent().build();
    }
}
