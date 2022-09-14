package com.ai.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

import org.springframework.web.context.annotation.ApplicationScope;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@ApplicationScope
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String message;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime created;

    @ManyToOne
    private User user;

}
