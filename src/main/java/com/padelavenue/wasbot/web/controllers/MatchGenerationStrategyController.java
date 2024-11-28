package com.padelavenue.wasbot.web.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.padelavenue.wasbot.services.generators.DynamicStrategyManager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/matches/strategy")
@RequiredArgsConstructor
@Tag(name = "Match Generation Strategy", description = "Match generation strategy management endpoints")
public class MatchGenerationStrategyController {

    private final DynamicStrategyManager strategyManager;

    @GetMapping("/current")
    @Operation(summary = "Get current match generation strategy")
    @ApiResponse(responseCode = "200", description = "Current strategy name")
    public ResponseEntity<String> getCurrentStrategy() {
        return ResponseEntity.ok(strategyManager.getCurrentStrategy());
    }

    @PutMapping
    @Operation(summary = "Update match generation strategy")
    @ApiResponse(responseCode = "200", description = "Strategy updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid strategy name")
    public ResponseEntity<String> updateStrategy(
            @Parameter(description = "New strategy name (randomStrategy, balancedStrategy, probabilisticStrategy)") 
            @RequestParam String strategy) {
        strategyManager.setStrategy(strategy);
        return ResponseEntity.ok("Strategy updated to: " + strategy);
    }

    @GetMapping("/available")
    @Operation(summary = "Get available strategy names")
    @ApiResponse(responseCode = "200", description = "List of available strategies")
    public ResponseEntity<List<String>> getAvailableStrategies() {
        return ResponseEntity.ok(List.of(
            "randomStrategy",
            "balancedStrategy",
            "probabilisticStrategy"
        ));
    }
}