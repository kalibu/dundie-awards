package com.ninjaone.dundie_awards.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "occured_at")
    private LocalDateTime occuredAt;

    @Column(name = "event")
    private String event;

    public Activity(LocalDateTime localDateTime, String event) {
        super();
        this.occuredAt = localDateTime;
        this.event = event;
    }

}
