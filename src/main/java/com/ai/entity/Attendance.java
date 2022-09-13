package com.ai.entity;

import org.springframework.format.annotation.DateTimeFormat;
import lombok.*;
import java.time.LocalDate;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class Attendance implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String attendanceStatus;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne
    private User user;

    @OneToOne
    private Batch batch;
}
