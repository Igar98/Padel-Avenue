package com.padelavenue.wasbot.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PlayerDto {
    @NotBlank(message = "validation.player.name.required")
    private String name;

    @NotBlank(message = "validation.player.phone.required")
    @Pattern(regexp = "^\\+?\\d{9,15}$", message = "validation.player.phone.invalid")
    private String phone;

    private Boolean isAdmin;

    private Integer points;

    private Boolean isActive;
}
