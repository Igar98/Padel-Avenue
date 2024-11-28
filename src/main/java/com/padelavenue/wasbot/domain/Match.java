package com.padelavenue.wasbot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

@Data
@Entity
public class Match {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime playedAt;
    
    //TODO: Change this when changing to postgresql
    // @Formula("DATE(DATE_TRUNC('week', played_at))")
    @Formula("PARSEDATETIME(FORMATDATETIME(played_at, 'yyyy-MM-dd'), 'yyyy-MM-dd')")
    private LocalDate weekOf;
    
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Player team1Player1;
    
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Player team1Player2;
    
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Player team2Player1;
    
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Player team2Player2;
    
    @NotNull
    private Boolean team1Won;
}
