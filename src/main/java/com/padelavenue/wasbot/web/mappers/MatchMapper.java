package com.padelavenue.wasbot.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.padelavenue.wasbot.domain.Match;
import com.padelavenue.wasbot.web.dto.MatchResponseDto;


//nullValuePropertyMappingStrategy will prevent null values from being mapped
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MatchMapper {
    
    @Mapping(target = "team1Player1Name", source = "team1Player1.name")
    @Mapping(target = "team1Player2Name", source = "team1Player2.name")
    @Mapping(target = "team2Player1Name", source = "team2Player1.name")
    @Mapping(target = "team2Player2Name", source = "team2Player2.name")
    MatchResponseDto toResponseDto(Match match);
}
