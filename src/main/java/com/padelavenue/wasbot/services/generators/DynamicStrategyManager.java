package com.padelavenue.wasbot.services.generators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DynamicStrategyManager {
    
    @Value("${match.generation.strategy:randomStrategy}")
    private String currentStrategy;
    
    public synchronized void setStrategy(String strategy) {
        if (!isValidStrategy(strategy)) {
            throw new IllegalArgumentException("Invalid strategy: " + strategy);
        }
        this.currentStrategy = strategy;
    }
    
    public String getCurrentStrategy() {
        return currentStrategy;
    }
    
    private boolean isValidStrategy(String strategy) {
        return strategy != null && (
            strategy.equals("randomStrategy") ||
            strategy.equals("balancedStrategy") ||
            strategy.equals("probabilisticStrategy")
        );
    }
}