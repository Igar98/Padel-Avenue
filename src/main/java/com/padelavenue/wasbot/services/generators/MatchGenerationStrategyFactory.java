package com.padelavenue.wasbot.services.generators;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MatchGenerationStrategyFactory {
    
    private final ApplicationContext context;

    private final DynamicStrategyManager strategyManager;

    public MatchGenerationStrategy getStrategy() {
        return (MatchGenerationStrategy) context.getBean(strategyManager.getCurrentStrategy());
    }
}
