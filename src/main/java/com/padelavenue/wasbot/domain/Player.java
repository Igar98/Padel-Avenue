package com.padelavenue.wasbot.domain;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class Player {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;
    
    @NotBlank
    @Pattern(regexp = "^\\+?\\d{9,15}$")
    @Column(nullable = false, unique = true)
    private String phone;
    
    private Boolean isAdmin = false;
    
    private Integer points = 0;
    
    private Boolean isActive = true;
}
