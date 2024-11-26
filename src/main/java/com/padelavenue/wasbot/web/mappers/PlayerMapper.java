package com.padelavenue.wasbot.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.padelavenue.wasbot.domain.Player;
import com.padelavenue.wasbot.web.dto.PlayerDto;


//nullValuePropertyMappingStrategy will prevent null values from being mapped
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PlayerMapper {

    PlayerDto toDto(Player player);

    @Mapping(target = "id", ignore = true)
    Player toEntity(PlayerDto playerDto);
    
    @Mapping(target = "id", ignore = true)
    void updatePlayerFromDto(PlayerDto playerDto, @MappingTarget Player player);
}
