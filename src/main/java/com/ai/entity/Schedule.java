package com.ai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import groovy.transform.builder.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    @NotNull(message = "Schedule date is required!")
    private LocalDate date;

    @ManyToOne
    private Batch batch;

    @OneToOne
    private Module module;

    @Transient
    private int batchId;

    @Transient
    private int moduleId;

}
