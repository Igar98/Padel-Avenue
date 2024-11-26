package com.padelavenue.wasbot.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerDto {
    @NotBlank(message = "validation.player.name.required")
    private String name;

    @NotBlank(message = "validation.player.phone.required")
    @Pattern(regexp = "^\\+?\\d{9,15}$", message = "validation.player.phone.invalid")
    private String phone;

    @Builder.Default
    private Boolean isAdmin = false;
    
    @Builder.Default
    private Integer points = 0;
    
    @Builder.Default
    private Boolean isActive = true;
}
