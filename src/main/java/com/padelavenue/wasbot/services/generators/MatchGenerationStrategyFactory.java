package com.padelavenue.wasbot.services.generators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MatchGenerationStrategyFactory {
    private final ApplicationContext context;

    @Value("${match.generation.strategy:randomStrategy}")
    private String defaultStrategy;

    public MatchGenerationStrategy getStrategy() {
        return (MatchGenerationStrategy) context.getBean(defaultStrategy);
    }
}
